package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.qrCode.QrCodeResponse;
import com.kimhok.tickets.dto.qrCode.QrCodeUpdateRequest;
import com.kimhok.tickets.entity.QrCode;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface QrCodeMapper {
    QrCodeResponse toResponse(QrCode entity);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "ticket", ignore = true),
            @Mapping(target = "value", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    void updateEntityFromRequest(QrCodeUpdateRequest request, @MappingTarget QrCode entity);
}
