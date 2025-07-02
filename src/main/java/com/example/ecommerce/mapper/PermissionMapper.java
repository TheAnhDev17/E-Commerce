package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.user.PermissionRequest;
import com.example.ecommerce.dto.response.user.PermissionResponse;
import com.example.ecommerce.entity.Permission;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
