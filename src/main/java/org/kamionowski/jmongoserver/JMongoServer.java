package org.kamionowski.jmongoserver;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.kamionowski.jmongoserver.core.MongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * User: soldier
 * Date: 05.06.12
 * Time: 19:43
 */
public class JMongoServer {
    private static final Logger log = LoggerFactory.getLogger(JMongoServer.class);
    final private IoAcceptor acceptor;
    final private int port;

    public JMongoServer(int port) throws IOException {
        this.acceptor = new NioSocketAcceptor();
        this.port = port;
        log.info("Starting...");
        //acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new WireProtocolCodecFactory()));

        acceptor.setHandler(new MongoHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        log.info("Listen on " + port);
    }

    public void listen() throws IOException {
        acceptor.bind(new InetSocketAddress(port));
    }

    public void dispose() {
        acceptor.dispose();
    }
}
