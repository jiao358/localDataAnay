package com.neko.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SupplyChainProductQueryDTO {
    private Integer page = 1;
    private Integer limit = 10;
    
    private String productName;
    private String categoryCode;
    private String supplierCompany;
    private BigDecimal minLimitPrice;
    private BigDecimal maxLimitPrice;
} 