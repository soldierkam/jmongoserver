/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.jmongoserver.db.admin.Command;
import org.kamionowski.jmongoserver.db.admin.CommandCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author soldier
 */
public class WhatsMyURICmd implements Command {

    private final static Logger log = LoggerFactory.getLogger(WhatsMyURICmd.class);
    private final CommandCollection collection;

    public WhatsMyURICmd(CommandCollection collection) {
        this.collection = collection;
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        SocketAddress remoteSocket = session.getRemoteAddress();
        if (!(remoteSocket instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Cannot convert " + remoteSocket + " to " + InetSocketAddress.class);
        }
        InetSocketAddress remote = (InetSocketAddress) remoteSocket;
        String you = remote.getAddress().getHostAddress() + ":" + remote.getPort();
        log.debug("You: " + you);
        BasicBSONObject result = new BasicBSONObject();
        result.append("you", you);
        result.append("ok", 1);
        return result;
    }

    @Override
    public String getName() {
        return "whatsmyuri";
    }
}
