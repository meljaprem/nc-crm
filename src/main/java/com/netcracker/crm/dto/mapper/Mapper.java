package com.netcracker.crm.dto.mapper;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public interface Mapper<F, T> {
      void configure(F from, T to);
}
