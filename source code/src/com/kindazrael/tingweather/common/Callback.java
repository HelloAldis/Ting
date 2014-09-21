
package com.kindazrael.tingweather.common;

public interface Callback< T, K extends Error> {

    public void success(T result);

    public void failure(K error);

}
