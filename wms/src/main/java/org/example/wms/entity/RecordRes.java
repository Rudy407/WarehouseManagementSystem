package org.example.wms.entity;

import lombok.Data;

@Data
public class RecordRes extends Record {
    private String username;
    private String adminname;
    private String goodsname;
    private String goodstypename;
    private String storagename;
}
