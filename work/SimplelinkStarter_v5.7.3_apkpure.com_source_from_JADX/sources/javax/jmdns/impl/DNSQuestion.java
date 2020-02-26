package javax.jmdns.impl;

import java.net.InetAddress;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceInfo.Fields;
import javax.jmdns.impl.DNSRecord.Address;
import javax.jmdns.impl.JmDNSImpl.ServiceTypeEntry;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public class DNSQuestion extends DNSEntry {
    private static Logger logger = Logger.getLogger(DNSQuestion.class.getName());

    private static class AllRecords extends DNSQuestion {
        public boolean isSameType(DNSEntry dNSEntry) {
            return dNSEntry != null;
        }

        AllRecords(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
            super(str, dNSRecordType, dNSRecordClass, z);
        }

        public void addAnswers(JmDNSImpl jmDNSImpl, Set<DNSRecord> set) {
            String lowerCase = getName().toLowerCase();
            if (jmDNSImpl.getLocalHost().getName().equalsIgnoreCase(lowerCase)) {
                set.addAll(jmDNSImpl.getLocalHost().answers(getRecordClass(), isUnique(), DNSConstants.DNS_TTL));
            } else if (jmDNSImpl.getServiceTypes().containsKey(lowerCase)) {
                new Pointer(getName(), DNSRecordType.TYPE_PTR, getRecordClass(), isUnique()).addAnswers(jmDNSImpl, set);
            } else {
                addAnswersForServiceInfo(jmDNSImpl, set, (ServiceInfoImpl) jmDNSImpl.getServices().get(lowerCase));
            }
        }

        public boolean iAmTheOnlyOne(JmDNSImpl jmDNSImpl) {
            String lowerCase = getName().toLowerCase();
            return jmDNSImpl.getLocalHost().getName().equals(lowerCase) || jmDNSImpl.getServices().keySet().contains(lowerCase);
        }
    }

    private static class DNS4Address extends DNSQuestion {
        DNS4Address(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
            super(str, dNSRecordType, dNSRecordClass, z);
        }

        public void addAnswers(JmDNSImpl jmDNSImpl, Set<DNSRecord> set) {
            Address dNSAddressRecord = jmDNSImpl.getLocalHost().getDNSAddressRecord(getRecordType(), true, DNSConstants.DNS_TTL);
            if (dNSAddressRecord != null) {
                set.add(dNSAddressRecord);
            }
        }

        public boolean iAmTheOnlyOne(JmDNSImpl jmDNSImpl) {
            String lowerCase = getName().toLowerCase();
            return jmDNSImpl.getLocalHost().getName().equals(lowerCase) || jmDNSImpl.getServices().keySet().contains(lowerCase);
        }
    }

    private static class DNS6Address extends DNSQuestion {
        DNS6Address(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
            super(str, dNSRecordType, dNSRecordClass, z);
        }

        public void addAnswers(JmDNSImpl jmDNSImpl, Set<DNSRecord> set) {
            Address dNSAddressRecord = jmDNSImpl.getLocalHost().getDNSAddressRecord(getRecordType(), true, DNSConstants.DNS_TTL);
            if (dNSAddressRecord != null) {
                set.add(dNSAddressRecord);
            }
        }

        public boolean iAmTheOnlyOne(JmDNSImpl jmDNSImpl) {
            String lowerCase = getName().toLowerCase();
            return jmDNSImpl.getLocalHost().getName().equals(lowerCase) || jmDNSImpl.getServices().keySet().contains(lowerCase);
        }
    }

    private static class HostInformation extends DNSQuestion {
        HostInformation(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
            super(str, dNSRecordType, dNSRecordClass, z);
        }
    }

    private static class Pointer extends DNSQuestion {
        Pointer(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
            super(str, dNSRecordType, dNSRecordClass, z);
        }

        public void addAnswers(JmDNSImpl jmDNSImpl, Set<DNSRecord> set) {
            for (ServiceInfo serviceInfo : jmDNSImpl.getServices().values()) {
                addAnswersForServiceInfo(jmDNSImpl, set, (ServiceInfoImpl) serviceInfo);
            }
            if (isServicesDiscoveryMetaQuery()) {
                for (String str : jmDNSImpl.getServiceTypes().keySet()) {
                    javax.jmdns.impl.DNSRecord.Pointer pointer = new javax.jmdns.impl.DNSRecord.Pointer("_services._dns-sd._udp.local.", DNSRecordClass.CLASS_IN, false, DNSConstants.DNS_TTL, ((ServiceTypeEntry) jmDNSImpl.getServiceTypes().get(str)).getType());
                    set.add(pointer);
                }
            } else if (isReverseLookup()) {
                String str2 = (String) getQualifiedNameMap().get(Fields.Instance);
                if (str2 != null && str2.length() > 0) {
                    InetAddress inetAddress = jmDNSImpl.getLocalHost().getInetAddress();
                    if (str2.equalsIgnoreCase(inetAddress != null ? inetAddress.getHostAddress() : "")) {
                        if (isV4ReverseLookup()) {
                            set.add(jmDNSImpl.getLocalHost().getDNSReverseAddressRecord(DNSRecordType.TYPE_A, false, DNSConstants.DNS_TTL));
                        }
                        if (isV6ReverseLookup()) {
                            set.add(jmDNSImpl.getLocalHost().getDNSReverseAddressRecord(DNSRecordType.TYPE_AAAA, false, DNSConstants.DNS_TTL));
                        }
                    }
                }
            } else {
                isDomainDiscoveryQuery();
            }
        }
    }

    private static class Service extends DNSQuestion {
        Service(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
            super(str, dNSRecordType, dNSRecordClass, z);
        }

        public void addAnswers(JmDNSImpl jmDNSImpl, Set<DNSRecord> set) {
            String lowerCase = getName().toLowerCase();
            if (jmDNSImpl.getLocalHost().getName().equalsIgnoreCase(lowerCase)) {
                set.addAll(jmDNSImpl.getLocalHost().answers(getRecordClass(), isUnique(), DNSConstants.DNS_TTL));
            } else if (jmDNSImpl.getServiceTypes().containsKey(lowerCase)) {
                new Pointer(getName(), DNSRecordType.TYPE_PTR, getRecordClass(), isUnique()).addAnswers(jmDNSImpl, set);
            } else {
                addAnswersForServiceInfo(jmDNSImpl, set, (ServiceInfoImpl) jmDNSImpl.getServices().get(lowerCase));
            }
        }

        public boolean iAmTheOnlyOne(JmDNSImpl jmDNSImpl) {
            String lowerCase = getName().toLowerCase();
            return jmDNSImpl.getLocalHost().getName().equals(lowerCase) || jmDNSImpl.getServices().keySet().contains(lowerCase);
        }
    }

    private static class Text extends DNSQuestion {
        Text(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
            super(str, dNSRecordType, dNSRecordClass, z);
        }

        public void addAnswers(JmDNSImpl jmDNSImpl, Set<DNSRecord> set) {
            addAnswersForServiceInfo(jmDNSImpl, set, (ServiceInfoImpl) jmDNSImpl.getServices().get(getName().toLowerCase()));
        }

        public boolean iAmTheOnlyOne(JmDNSImpl jmDNSImpl) {
            String lowerCase = getName().toLowerCase();
            return jmDNSImpl.getLocalHost().getName().equals(lowerCase) || jmDNSImpl.getServices().keySet().contains(lowerCase);
        }
    }

    public void addAnswers(JmDNSImpl jmDNSImpl, Set<DNSRecord> set) {
    }

    public boolean iAmTheOnlyOne(JmDNSImpl jmDNSImpl) {
        return false;
    }

    public boolean isExpired(long j) {
        return false;
    }

    public boolean isStale(long j) {
        return false;
    }

    public void toString(StringBuilder sb) {
    }

    DNSQuestion(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
        super(str, dNSRecordType, dNSRecordClass, z);
    }

    public static DNSQuestion newQuestion(String str, DNSRecordType dNSRecordType, DNSRecordClass dNSRecordClass, boolean z) {
        switch (dNSRecordType) {
            case TYPE_A:
                return new DNS4Address(str, dNSRecordType, dNSRecordClass, z);
            case TYPE_A6:
                return new DNS6Address(str, dNSRecordType, dNSRecordClass, z);
            case TYPE_AAAA:
                return new DNS6Address(str, dNSRecordType, dNSRecordClass, z);
            case TYPE_ANY:
                return new AllRecords(str, dNSRecordType, dNSRecordClass, z);
            case TYPE_HINFO:
                return new HostInformation(str, dNSRecordType, dNSRecordClass, z);
            case TYPE_PTR:
                return new Pointer(str, dNSRecordType, dNSRecordClass, z);
            case TYPE_SRV:
                return new Service(str, dNSRecordType, dNSRecordClass, z);
            case TYPE_TXT:
                return new Text(str, dNSRecordType, dNSRecordClass, z);
            default:
                return new DNSQuestion(str, dNSRecordType, dNSRecordClass, z);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean answeredBy(DNSEntry dNSEntry) {
        return isSameRecordClass(dNSEntry) && isSameType(dNSEntry) && getName().equals(dNSEntry.getName());
    }

    /* access modifiers changed from: protected */
    public void addAnswersForServiceInfo(JmDNSImpl jmDNSImpl, Set<DNSRecord> set, ServiceInfoImpl serviceInfoImpl) {
        if (serviceInfoImpl != null && serviceInfoImpl.isAnnounced()) {
            if (getName().equalsIgnoreCase(serviceInfoImpl.getQualifiedName()) || getName().equalsIgnoreCase(serviceInfoImpl.getType()) || getName().equalsIgnoreCase(serviceInfoImpl.getTypeWithSubtype())) {
                set.addAll(jmDNSImpl.getLocalHost().answers(getRecordClass(), true, DNSConstants.DNS_TTL));
                set.addAll(serviceInfoImpl.answers(getRecordClass(), true, DNSConstants.DNS_TTL, jmDNSImpl.getLocalHost()));
            }
            if (logger.isLoggable(Level.FINER)) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append(jmDNSImpl.getName());
                sb.append(" DNSQuestion(");
                sb.append(getName());
                sb.append(").addAnswersForServiceInfo(): info: ");
                sb.append(serviceInfoImpl);
                sb.append("\n");
                sb.append(set);
                logger2.finer(sb.toString());
            }
        }
    }
}
