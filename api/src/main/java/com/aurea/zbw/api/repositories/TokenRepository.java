package com.aurea.zbw.api.repositories;

import com.aurea.zbw.api.model.Token;
import com.aurea.zbw.api.model.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TokenRepository extends CrudRepository<Token, String> {

    @ApiOperation(value = "findOneValidToken")
    @Query(value = "SELECT t FROM Token t WHERE t.id = ?1 and t.expirationDate > current_timestamp")
    Token findOneValidToken(@Param("token") String token);

}
