# Kaleo
Kaleo is a service running on  [Play Framework](https://playframework.com/ "Play Framework"), able to run concurrent HTTP/HTTPS calls to remote services.

It returns a json structure containing the list of responses from the single service called.


## Requirements
It has been developed an tested on Play Framework 2.5.

- Java 8.X
- Play Framework 2.5.X

## Getting Started 
ToDo

## How To invoke it
Kaleo expones just one API for **HTTP POST** method:
<code>
http://hostname:9000/geturldata
</code>

The payload of data posted should be a json containing a key-value structure, where *key* is just a **callId** and *value* is the **service's url** to be called.
e.g.

```
{
"c1":"http://localhost:8983/solr/attributes/select?q=uid:277&wt=json",
"c2":"http://localhost:8983/solr/attributes/select?q=uid:273&wt=json",
"c3":"http://localhost:8983/solr/attributes/select?q=*:*&wt=json&rows=1"
}
```

The *client_for_test* directory contains a curl based script and the data example to run a test.

