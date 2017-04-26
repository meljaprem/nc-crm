package com.netcracker.crm.dao.impl;

import com.netcracker.crm.dao.UserAttemptsDao;
import com.netcracker.crm.dao.impl.sql.UserSqlQuery;
import com.netcracker.crm.domain.UserAttempts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.netcracker.crm.dao.impl.sql.UserAttemptSqlQuery.*;


/**
 * Created by Pasha on 22.04.2017.
 */
@Repository
public class UserAttemptsDaoImpl implements UserAttemptsDao {
    private static final int MAX_ATTEMPTS = 3;
    private static final Logger log = LoggerFactory.getLogger(UserAttemptsDaoImpl.class);
    private NamedParameterJdbcTemplate namedJdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void updateFailAttempts(String userMail) {

        UserAttempts user = getUserAttempts(userMail);
        if (user == null) {
            if (isUserExists(userMail)) {
                create(userMail);
            }
        } else {
            if (isUserExists(userMail)) {
                update(userMail);
            }
            if (user.getAttempts() + 1 >= MAX_ATTEMPTS) {
                lockUserAccount(userMail, true);
                throw new LockedException("User Account is locked!");
            }
        }

    }

    @Override
    public UserAttempts getUserAttempts(String userMail) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_EMAIL, userMail);
        return namedJdbcTemplate.query(SQL_USER_ATTEMPTS_GET, params, new UserAttemptsWithDetailExtractor());
    }

    @Override
    public boolean resetFailAttempts(String userMail) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_EMAIL, userMail);
        int count = namedJdbcTemplate.update(SQL_USER_ATTEMPTS_RESET_ATTEMPTS, params);
        if (count == 1) {
            log.info("Reset UserAttempts with email : " + userMail + " is successful");
            return true;
        } else if (count > 1) {
            log.error("Reset UserAttempts more 1 rows");
            return false;
        } else {
            log.error("Reset UserAttempts 0 rows");
            return false;
        }
    }

    @Override
    public boolean lockUserAccount(String userMail, boolean lock) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_EMAIL, userMail)
                .addValue(UserSqlQuery.PARAM_USER_ACCOUNT_NON_LOCKED, !lock);
        int count = namedJdbcTemplate.update(UserSqlQuery.SQL_USERS_UPDATE_LOCKED, params);
        if (count == 1) {
            log.info("Update user with email : " + userMail + " is successful");
            return true;
        } else if (count > 1) {
            log.error("Update more 1 rows");
            return false;
        } else {
            log.info("Update 0 rows");
            return false;
        }
    }

    private boolean isUserExists(String userMail) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_EMAIL, userMail);
        int count = namedJdbcTemplate.queryForObject(UserSqlQuery.SQL_USERS_COUNT, params, Integer.class);
        return count > 0;
    }


    private long create(String userMail) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_EMAIL, userMail)
                .addValue(PARAM_ATTEMPTS, 1)
                .addValue(PARAM_LAST_MODIFIED, new Date());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int affectedRows = namedJdbcTemplate.update(SQL_USER_ATTEMPTS_INSERT, params, keyHolder);
        if (affectedRows == 1) {
            Long id = (Long) keyHolder.getKeys().get(PARAM_ID);
            log.info("UserAttempts with id: " + id + " is successfully created.");
            return id;
        } else {
            log.error("UserAttempts is not created.");
            return -1L;
        }
    }

    private long update(String userMail) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_LAST_MODIFIED, new Date())
                .addValue(PARAM_EMAIL, userMail);
        int affectedRows = namedJdbcTemplate.update(SQL_USER_ATTEMPTS_UPDATE_ATTEMPTS, params);
        if (affectedRows == 1) {
            log.info("UserAttempts with email : " + userMail + " is successfully update.");
            return 1L;
        } else {
            log.error("UserAttempts is not updated.");
            return -1L;
        }
    }


    private static final class UserAttemptsWithDetailExtractor implements ResultSetExtractor<UserAttempts> {

        @Override
        public UserAttempts extractData(ResultSet rs) throws SQLException, DataAccessException {
            UserAttempts userAttempts = null;
            while (rs.next()) {
                userAttempts = new UserAttempts();
                userAttempts.setId(rs.getInt(PARAM_ID));
                userAttempts.setUserMail(rs.getString(PARAM_EMAIL));
                userAttempts.setAttempts(rs.getInt(PARAM_ATTEMPTS));
                userAttempts.setLastModified(rs.getTimestamp(PARAM_LAST_MODIFIED));
            }
            return userAttempts;
        }
    }
}