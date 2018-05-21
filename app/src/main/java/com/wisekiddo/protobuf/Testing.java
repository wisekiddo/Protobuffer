package com.wisekiddo.protobuf;

import com.wisekiddo.protobuf.mokapos.ItemsOuterClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ronald on 11/5/18.
 */

public class Testing {

    public static void main(String[] args){
        String fileName = "/Users/ronald/Downloads/encrypted_binary/proto1";
        try {
            ItemsOuterClass.Items test = ItemsOuterClass.Items.parseFrom(new FileInputStream(fileName));
            System.out.println(test);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
