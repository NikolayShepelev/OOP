package org.investment;

public class SecurityRepository extends AbstractSecurityRepository {
    public SecurityRepository(String filename, ISerializationStrategy strategy) {
        super(filename, strategy);
    }
}
