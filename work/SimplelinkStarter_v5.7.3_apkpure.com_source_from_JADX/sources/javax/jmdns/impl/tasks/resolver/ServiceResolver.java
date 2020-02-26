package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.DNSRecord.Pointer;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public class ServiceResolver extends DNSResolverTask {
    private final String _type;

    /* access modifiers changed from: protected */
    public String description() {
        return "querying service";
    }

    public ServiceResolver(JmDNSImpl jmDNSImpl, String str) {
        super(jmDNSImpl);
        this._type = str;
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceResolver(");
        sb.append(getDns() != null ? getDns().getName() : "");
        sb.append(")");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing addAnswers(DNSOutgoing dNSOutgoing) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        for (ServiceInfo serviceInfo : getDns().getServices().values()) {
            Pointer pointer = new Pointer(serviceInfo.getType(), DNSRecordClass.CLASS_IN, false, DNSConstants.DNS_TTL, serviceInfo.getQualifiedName());
            dNSOutgoing = addAnswer(dNSOutgoing, (DNSRecord) pointer, currentTimeMillis);
        }
        return dNSOutgoing;
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing addQuestions(DNSOutgoing dNSOutgoing) throws IOException {
        return addQuestion(dNSOutgoing, DNSQuestion.newQuestion(this._type, DNSRecordType.TYPE_PTR, DNSRecordClass.CLASS_IN, false));
    }
}
