package org.wallet.service.common.mysql;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * @author zengfucheng
 **/
@SuppressWarnings("deprecation")
public class MySQL5DialectUTF8 extends MySQL5InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}

