package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.DNSRecord.Pointer;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.JmDNSImpl.ServiceTypeEntry;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public class TypeResolver extends DNSResolverTask {
    /* access modifiers changed from: protected */
    public String description() {
        return "querying type";
    }

    public TypeResolver(JmDNSImpl jmDNSImpl) {
        super(jmDNSImpl);
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("TypeResolver(");
        sb.append(getDns() != null ? getDns().getName() : "");
        sb.append(")");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing addAnswers(DNSOutgoing dNSOutgoing) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        for (String str : getDns().getServiceTypes().keySet()) {
            String str2 = "_services._dns-sd._udp.local.";
            Pointer pointer = new Pointer(str2, DNSRecordClass.CLASS_IN, false, DNSConstants.DNS_TTL, ((ServiceTypeEntry) getDns().getServiceTypes().get(str)).getType());
            dNSOutgoing = addAnswer(dNSOutgoing, (DNSRecord) pointer, currentTimeMillis);
        }
        return dNSOutgoing;
    }

    /* access modifiers changed from: protected */
    public DNSOutgoing addQuestions(DNSOutgoing dNSOutgoing) throws IOException {
        return addQuestion(dNSOutgoing, DNSQuestion.newQuestion("_services._dns-sd._udp.local.", DNSRecordType.TYPE_PTR, DNSRecordClass.CLASS_IN, false));
    }
}
