# Probe Subsystem


This is a module to be deployed into WildFlyAS and which then takes
measurements from the WildFly management model and sends to a storage subsystem.

## Prerequisites

### Wildfly 8.1.0

Get and install Wildfly 8.1.0: http://download.jboss.org/wildfly/8.1.0.Final/wildfly-8.1.0.Final.zip

It's currently been tested against WF 8.1.0 and the default server configuration (standalone-monitor.xml) is configured for WF 8.
But apart from that there should be no reason to not use it on WF 9.

### The storage subsystem

http://github.com/mono-plane/mono-storage

## Build & Install

Build the top level project:

`
$ mvn clean install
`

This will also create a probe-module.zip, that can be installed on Wildfly:

`unzip target/probe-module.zip -d $WILDFLY_HOME`

This will add an additional module that contains the probe extension and subsystem:

`modules/system/layers/base/org/wildfly/extension/monoplane/probe/`

## Package Contents

The following contents will be installed when you unpack the #probe-module.zip:

```
modules/system/layers/base/org/wildfly/extension/monoplane/probe/main/module.xml (1)
modules/system/layers/base/org/org/wildfly/extension/monoplane/probe/main/*.jar (2)
standalone/configuration/standalone-probe.xml (3)
domain/configuration/porbe-domain.xml (4)
domain/configuration/probe-host.xml (5)`
```

1. The module descriptor
2. Required libraries to run the probe on Wildfly
3. An example configuration for standalone servers
4. An example configuration for managed domains
5. An example host configuration

## Server Configuration Profiles

The probe-module.zip server profiles for both standalone and domain mode that can be used to start a pre-configured Wildfly instance:

### Standalone Mode

`./bin/standalone.sh -c standalone-probe.xml -b 127.0.0.1`

### Domain Mode

`./bin/domain.sh --domain-config=probe-domain.xml --host-config=probe-host.xml -b 127.0.0.1`


## Get In touch

The best way to reach out and discuss the subsystem is the Wildfly mailing list and/or the Chat Room:

- Mailing List: https://lists.jboss.org/mailman/listinfo/wildfly-dev
- IRC: irc://freenode.org/#wildfly

## License

- http://www.apache.org/licenses/LICENSE-2.0.html

## Resources
- https://docs.jboss.org/author/display/WFLY8/Documentation

