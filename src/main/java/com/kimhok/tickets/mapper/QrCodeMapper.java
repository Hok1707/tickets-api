package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.qrCode.QrCodeDto;
import com.kimhok.tickets.entity.QrCode;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface QrCodeMapper {
    QrCode toEntity(QrCodeDto qrCodeDto);

    QrCodeDto toDto(QrCode qrCode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    QrCode partialUpdate(QrCodeDto qrCodeDto, @MappingTarget QrCode qrCode);
}