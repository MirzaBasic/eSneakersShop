package hci_ib130163.sneakers.Helper;

import java.io.Serializable;

/**
 * Created by Developer on 10.09.2016..
 */
public interface MyRunnable<T> extends Serializable{

    void run(T t);


}
