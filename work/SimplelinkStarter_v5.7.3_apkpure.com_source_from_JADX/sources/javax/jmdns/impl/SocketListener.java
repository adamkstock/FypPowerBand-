package javax.jmdns.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.impl.constants.DNSConstants;

class SocketListener extends Thread {
    static Logger logger = Logger.getLogger(SocketListener.class.getName());
    private final JmDNSImpl _jmDNSImpl;

    SocketListener(JmDNSImpl jmDNSImpl) {
        StringBuilder sb = new StringBuilder();
        sb.append("SocketListener(");
        sb.append(jmDNSImpl != null ? jmDNSImpl.getName() : "");
        sb.append(")");
        super(sb.toString());
        setDaemon(true);
        this._jmDNSImpl = jmDNSImpl;
    }

    public void run() {
        String str = ".run() exception ";
        try {
            byte[] bArr = new byte[DNSConstants.MAX_MSG_ABSOLUTE];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            while (!this._jmDNSImpl.isCanceling() && !this._jmDNSImpl.isCanceled()) {
                datagramPacket.setLength(bArr.length);
                this._jmDNSImpl.getSocket().receive(datagramPacket);
                if (this._jmDNSImpl.isCanceling() || this._jmDNSImpl.isCanceled() || this._jmDNSImpl.isClosing() || this._jmDNSImpl.isClosed()) {
                    break;
                }
                try {
                    if (!this._jmDNSImpl.getLocalHost().shouldIgnorePacket(datagramPacket)) {
                        DNSIncoming dNSIncoming = new DNSIncoming(datagramPacket);
                        if (dNSIncoming.isValidResponseCode()) {
                            if (logger.isLoggable(Level.FINEST)) {
                                Logger logger2 = logger;
                                StringBuilder sb = new StringBuilder();
                                sb.append(getName());
                                sb.append(".run() JmDNS in:");
                                sb.append(dNSIncoming.print(true));
                                logger2.finest(sb.toString());
                            }
                            if (dNSIncoming.isQuery()) {
                                if (datagramPacket.getPort() != DNSConstants.MDNS_PORT) {
                                    this._jmDNSImpl.handleQuery(dNSIncoming, datagramPacket.getAddress(), datagramPacket.getPort());
                                }
                                this._jmDNSImpl.handleQuery(dNSIncoming, this._jmDNSImpl.getGroup(), DNSConstants.MDNS_PORT);
                            } else {
                                this._jmDNSImpl.handleResponse(dNSIncoming);
                            }
                        } else if (logger.isLoggable(Level.FINE)) {
                            Logger logger3 = logger;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(getName());
                            sb2.append(".run() JmDNS in message with error code:");
                            sb2.append(dNSIncoming.print(true));
                            logger3.fine(sb2.toString());
                        }
                    }
                } catch (IOException e) {
                    Logger logger4 = logger;
                    Level level = Level.WARNING;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(getName());
                    sb3.append(str);
                    logger4.log(level, sb3.toString(), e);
                }
            }
        } catch (IOException e2) {
            if (!this._jmDNSImpl.isCanceling() && !this._jmDNSImpl.isCanceled() && !this._jmDNSImpl.isClosing() && !this._jmDNSImpl.isClosed()) {
                Logger logger5 = logger;
                Level level2 = Level.WARNING;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(getName());
                sb4.append(str);
                logger5.log(level2, sb4.toString(), e2);
                this._jmDNSImpl.recover();
            }
        }
        if (logger.isLoggable(Level.FINEST)) {
            Logger logger6 = logger;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(getName());
            sb5.append(".run() exiting.");
            logger6.finest(sb5.toString());
        }
    }

    public JmDNSImpl getDns() {
        return this._jmDNSImpl;
    }
}
