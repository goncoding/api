package com.daesung.api.upload;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile02 attachFile;
    private List<UploadFile02> imageFiles;


}
