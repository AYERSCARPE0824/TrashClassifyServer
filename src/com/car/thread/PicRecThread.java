package com.car.thread;

import com.car.dao.ClassifyDao;
import com.car.dao.LableDao;
import com.car.dao.imp.ClassifyDaoImp;
import com.car.dao.imp.LableDaoImp;
import com.car.request.PicRecRequest;
import com.car.result.RecResult;
import com.car.utils.Currency;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author jhyang
 * @create 2022-03-29 14:46
 */
public class PicRecThread extends BaseThread {
    private PicRecRequest request;
    private LableDao lableDao;
    private ClassifyDao classifyDao;

    public PicRecThread(PicRecRequest request, Socket socket) {
        this.request = request;
        this.socket = socket;
        lableDao = new LableDaoImp();
        classifyDao = new ClassifyDaoImp();
    }

    @Override
    public void run() {
        String result_Lable = null;
        String result_Name = null;
        byte[] bytes = request.getBytes();
        int len;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("src/image.jpg");
            fos.write(bytes);
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("not found");
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            result_Lable = new Currency().doPicRec();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result_Name = lableDao.checkNameFormLable(result_Lable);
//        result_Name = lableDao.checkNameFormLable("15460");
        String className = classifyDao.checkClassify(result_Name);

        RecResult recResult = new RecResult();
        if (className == null){
            recResult.setRes(false);
        } else {
            recResult.setRes(true);
            recResult.setClassName(className);
        }
        recResult.setResultType(RecResult.PIC_REC_RESULT);
        sendObject(recResult);
        System.out.println("send picrec result   time" + new Date());
    }
}
