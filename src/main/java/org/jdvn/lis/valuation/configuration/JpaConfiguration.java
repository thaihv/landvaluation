package org.jdvn.lis.valuation.configuration;

import org.jdvn.lis.valuation.domain.DomainObject;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = DomainObject.class)
public class JpaConfiguration {

}