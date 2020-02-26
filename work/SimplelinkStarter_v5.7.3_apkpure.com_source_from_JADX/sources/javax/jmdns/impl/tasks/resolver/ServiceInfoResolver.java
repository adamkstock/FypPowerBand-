package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import javax.jmdns.impl.DNSEntry;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public class ServiceInfoResolver extends DNSResolverTask {
    private final ServiceInfoImpl _info;

    public ServiceInfoResolver(JmDNSImpl jmDNSImpl, ServiceInfoImpl serviceInfoImpl) {
        super(jmDNSImpl);
        this._info = serviceInfoImpl;
        serviceInfoImpl.setDns(getDns());
        getDns().addListener(serviceInfoImpl, DNSQuestion.newQuestion(serviceInfoImpl.getQualifiedName(), DNSRecordType.TYPE_ANY, DNSRecordClass.CLASS_IN, false));
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceInfoResolver(");
        sb.append(getDns() != null ? getDns().getName() : "");
        sb.append(")");
        return sb.toString();
    }

    public boolean cancel() {
        boolean cancel = super.cancel();
        if (!this._info.isPersistent()) {
            getDns().removeListener(this._info);
        }
        return cancel;
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing addAnswers(DNSOutgoing dNSOutgoing) throws IOException {
        if (!this._info.hasData()) {
            long currentTimeMillis = System.currentTimeMillis();
            dNSOutgoing = addAnswer(addAnswer(dNSOutgoing, (DNSRecord) getDns().getCache().getDNSEntry(this._info.getQualifiedName(), DNSRecordType.TYPE_SRV, DNSRecordClass.CLASS_IN), currentTimeMillis), (DNSRecord) getDns().getCache().getDNSEntry(this._info.getQualifiedName(), DNSRecordType.TYPE_TXT, DNSRecordClass.CLASS_IN), currentTimeMillis);
            if (this._info.getServer().length() > 0) {
                for (DNSEntry dNSEntry : getDns().getCache().getDNSEntryList(this._info.getServer(), DNSRecordType.TYPE_A, DNSRecordClass.CLASS_IN)) {
                    dNSOutgoing = addAnswer(dNSOutgoing, (DNSRecord) dNSEntry, currentTimeMillis);
                }
                for (DNSEntry dNSEntry2 : getDns().getCache().getDNSEntryList(this._info.getServer(), DNSRecordType.TYPE_AAAA, DNSRecordClass.CLASS_IN)) {
                    dNSOutgoing = addAnswer(dNSOutgoing, (DNSRecord) dNSEntry2, currentTimeMillis);
                }
            }
        }
        return dNSOutgoing;
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing addQuestions(DNSOutgoing dNSOutgoing) throws IOException {
        if (this._info.hasData()) {
            return dNSOutgoing;
        }
        DNSOutgoing addQuestion = addQuestion(addQuestion(dNSOutgoing, DNSQuestion.newQuestion(this._info.getQualifiedName(), DNSRecordType.TYPE_SRV, DNSRecordClass.CLASS_IN, false)), DNSQuestion.newQuestion(this._info.getQualifiedName(), DNSRecordType.TYPE_TXT, DNSRecordClass.CLASS_IN, false));
        return this._info.getServer().length() > 0 ? addQuestion(addQuestion(addQuestion, DNSQuestion.newQuestion(this._info.getServer(), DNSRecordType.TYPE_A, DNSRecordClass.CLASS_IN, false)), DNSQuestion.newQuestion(this._info.getServer(), DNSRecordType.TYPE_AAAA, DNSRecordClass.CLASS_IN, false)) : addQuestion;
    }

    /* access modifiers changed from: protected */
    public String description() {
        StringBuilder sb = new StringBuilder();
        sb.append("querying service info: ");
        ServiceInfoImpl serviceInfoImpl = this._info;
        sb.append(serviceInfoImpl != null ? serviceInfoImpl.getQualifiedName() : "null");
        return sb.toString();
    }
}
