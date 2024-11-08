package org.investment;

import java.util.List;

public interface ISecurityRepository {
    Security getById(int id);
    List<BriefSecurity> get_k_n_short_list(int k, int n, String sortField);
    void addSecurity(Security security);
    void replaceSecurity(int id, Security newSecurity);
    void deleteSecurity(int id);
    int get_count();
    void sort_by_field(String field);
}