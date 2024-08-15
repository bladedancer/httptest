Launch an envoy proxy on 8081.

func-e  run -c envoy/envoy.yaml

Run artillery against it
artillery run envoy/art.yaml