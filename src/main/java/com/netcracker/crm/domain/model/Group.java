package com.netcracker.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface Group {

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    Discount getDiscount();

    void setDiscount(Discount discount);
}
