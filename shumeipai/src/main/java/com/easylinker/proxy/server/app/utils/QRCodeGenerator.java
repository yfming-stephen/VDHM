package com.easylinker.proxy.server.app.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by wwhai on 2017/10/5.
 * 二维码生成器
        */
public class QRCodeGenerator {
    public static final int WIDTH = 150;
    public static final int HEIGHT = 150;
    private static int ON_COLOR = 0xFF000000;     //前景色
    private static int BACK_COLOR = 0xFFFFFFFF;    //背景色

    public static File string2BarCode(String deviceId) {
        File file = null;
        try {
            file = new File("./tempBarCodeImage.png");
            OutputStream outputStream = new FileOutputStream(file);
            QRCodeWriter writer = new QRCodeWriter();
            //http://localhost/device/barCode/2423de45t46g465
            // //通过二维码的HTTP请求 就可以获取数据
            BitMatrix matrix = writer.encode(deviceId, BarcodeFormat.QR_CODE, HEIGHT, WIDTH);
            MatrixToImageConfig config = new MatrixToImageConfig(ON_COLOR, BACK_COLOR);

            MatrixToImageWriter.writeToStream(matrix, "png", outputStream, config);

        } catch (Exception e) {
            System.out.println("二维码生成失败!");

        }
        return file;
    }



}