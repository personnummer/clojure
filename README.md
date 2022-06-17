# clojure-personnummer

[![Clojure CI](https://github.com/bombsimon/clojure-personnummer/actions/workflows/clojure.yml/badge.svg?branch=main)](https://github.com/bombsimon/clojure-personnummer/actions/workflows/clojure.yml)

## Usage

```clojure
(ns my-app
  (:require [personnummer :as p]))

(let [pnr (p/personnummer "199001010017")]
  (format "The person with personal identity number %s is a %s of age %s"
                       (p/format pnr) (p/gender pnr) (p/age pnr)))
```

### Example

See [example](./example/) folder for a runnable example.

## Testing

With [`lein`](https://leiningen.org/)

```sh
% lein test
```
