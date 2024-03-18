package org.koregin.socks_app.mapper;

public interface GenericMapper<F, T> {

    T map(F object);

    default T map(F fromObject, T toObject) {
        return toObject;
    }
}
