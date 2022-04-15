package com.car.thread;

import com.car.dao.ClassifyDao;
import com.car.dao.imp.ClassifyDaoImp;
import com.car.dao.imp.LableDaoImp;
import com.car.request.PicRecRequest;
import com.car.request.WordRecRequest;
import com.car.result.RecResult;

import java.net.Socket;
import java.util.Date;

/**
 * @author jhyang
 * @create 2022-03-29 16:58
 */
public class WordRecThread extends BaseThread {
    private WordRecRequest request;
    private ClassifyDao classifyDao;

    public WordRecThread(WordRecRequest request, Socket socket) {
        this.request = request;
        this.socket = socket;
        this.classifyDao = new ClassifyDaoImp();
    }

    @Override
    public void run() {
        String word = request.getWord();
        String className = classifyDao.checkClassify(word);
        RecResult recResult = new RecResult();
        if (className == null){
            recResult.setRes(false);
        } else {
            recResult.setRes(true);
            recResult.setClassName(className);
        }
        recResult.setResultType(RecResult.WORD_REC_RESULT);
        sendObject(recResult);
        System.out.println("send wordrec result   time" + new Date());
    }
}
