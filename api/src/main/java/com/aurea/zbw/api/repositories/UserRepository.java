package com.aurea.zbw.api.repositories;

import com.aurea.zbw.api.model.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @ApiOperation(value = "findOneByUsername")
    User findOneByUsername(@Param("username") String username);

}
