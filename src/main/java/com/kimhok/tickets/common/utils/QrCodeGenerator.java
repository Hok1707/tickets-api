package com.kimhok.tickets.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

public class QrCodeGenerator {
    public static byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
//        declare call QR code write
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        call bit matrix for encode
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,width,height);
//        call byte array out put
        ByteArrayOutputStream pngOutPutStream =new ByteArrayOutputStream();
//        write it to image and set format
        MatrixToImageWriter.writeToStream(bitMatrix,"PNG",pngOutPutStream);
//        return output stream with byte array
        return pngOutPutStream.toByteArray();
    }

    public static String generateQRCodeBase64(String text,int width,int height) throws WriterException,IOException{
        byte[] pngData = generateQRCodeImage(text,width,height);
        return "data:image/png;base64,"+ Base64.getEncoder().encodeToString(pngData);
    }

    public static void generateQrCodeFile(String text,int width,int height,String filePath) throws WriterException,IOException{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text,BarcodeFormat.QR_CODE,width,height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
    }
}
