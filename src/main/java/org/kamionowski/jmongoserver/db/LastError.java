package org.kamionowski.jmongoserver.db;

import org.apache.mina.core.session.IoSession;

/**
 * User: soldier
 * Date: 04.06.12
 * Time: 21:40
 */
public class LastError {

    private final IoSession session;
    private final static String ERR = "lastErrorAtt.msg";
    private final static String N = "lastErrorAttr.n";

    public LastError(IoSession session) {
        this.session = session;
    }

    public String getMessage() {
        return (String) session.getAttribute(ERR);
    }

    public void setMessage(String error) {
        session.setAttribute(ERR, error);
    }

    public Integer getN() {
        return (Integer) session.getAttribute(N);
    }

    public void setN(Integer n) {
        session.setAttribute(N, n);
    }

    public void reset() {
        session.removeAttribute(ERR);
    }
}
