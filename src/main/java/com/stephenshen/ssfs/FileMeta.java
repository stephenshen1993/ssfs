package com.stephenshen.ssfs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * file meta data.
 *
 * @author stephenshen
 * @date 2024/7/20 18:26:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMeta {
    private String name;
    private String originalFileName;
    private long size;
    private Map<String, String> tags = new HashMap<>();
}
