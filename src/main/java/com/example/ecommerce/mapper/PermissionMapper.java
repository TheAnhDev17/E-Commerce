package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.RoleResponse;
import com.example.ecommerce.entity.Role;
import org.mapstruct.Mapper;


@Mapper
public interface RoleMapper {

    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
