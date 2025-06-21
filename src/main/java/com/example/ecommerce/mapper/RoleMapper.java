package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.RoleResponse;
import com.example.ecommerce.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
