(ns leiningen.new.descjop
  "Generate a basic Electron(atom-shell) application project."
  (:require [leiningen.new.templates :refer [renderer year project-name
                                             ->files sanitize-ns name-to-path
                                             multi-segment]]
            [leiningen.core.main :as main]))

(declare help-text)

(defn project-default
  "default descjop template."
  [name]
  (let [render (renderer "descjop")
        main-ns (multi-segment (sanitize-ns name))
        data {:raw-name name
              :name (project-name name)
              :sanitized (name-to-path name)
              :namespace main-ns
              :nested-dirs (name-to-path main-ns)
              :year (year)}]
    (main/info "Generating fresh descjop project.")
    (->files data
             ["app/index.html" (render "app__index.html" data)]
             ["app/js/externs.js" (render "app__js__externs.js" data)]
             ["app/js/main.js" (render "app__js__main.js" data)]
             ["app/package.json" (render "app__package.json" data)]
             ["Gruntfile.js" (render "Gruntfile.js" data)]
             [".gitignore" (render ".gitignore" data)]
             ["package.json" (render "package.json" data)]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["src/{{sanitized}}/core.cljs" (render "src__core.cljs" data)]
             "resources")))

(defn project-om
  "default descjop template with om.

  ## change from default
  core.cljs -> om__src__core.cljs

  ## change from default
  index.html -> om__app__index.html
  project.clj -> om__project.clj
  "
  [name]
  (let [render (renderer "descjop")
        main-ns (multi-segment (sanitize-ns name))
        data {:raw-name name
              :name (project-name name)
              :sanitized (name-to-path name)
              :namespace main-ns
              :nested-dirs (name-to-path main-ns)
              :year (year)}]
    (main/info "Generating fresh descjop +om project.")
    (->files data
             ["app/index.html" (render "om__app__index.html" data)]
             ["app/js/externs.js" (render "app__js__externs.js" data)]
             ["app/js/main.js" (render "app__js__main.js" data)]
             ["app/package.json" (render "app__package.json" data)]
             ["Gruntfile.js" (render "Gruntfile.js" data)]
             [".gitignore" (render ".gitignore" data)]
             ["package.json" (render "package.json" data)]
             ["project.clj" (render "om__project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["src/{{sanitized}}/core.cljs" (render "src__core.cljs" data)]
             ["src_front/{{sanitized}}_om/core.cljs" (render "om__src__core.cljs" data)]
             ["src_tools/figwheel_middleware.clj" (render "figwheel_middleware.clj" data)]
             "resources")))

(defn project-reagent
  "default descjop template with reagent.

  ## change from default
  core.cljs -> reagent__src__core.cljs

  ## change from default
  index.html -> reagent__app__index.html
  project.clj -> reagent__project.clj
  "
  [name]
  (let [render (renderer "descjop")
        main-ns (multi-segment (sanitize-ns name))
        data {:raw-name name
              :name (project-name name)
              :sanitized (name-to-path name)
              :namespace main-ns
              :nested-dirs (name-to-path main-ns)
              :year (year)}]
    (main/info "Generating fresh descjop +reagent project.")
    (->files data
             ["app/index.html" (render "reagent__app__index.html" data)]
             ["app/js/externs.js" (render "app__js__externs.js" data)]
             ["app/js/main.js" (render "app__js__main.js" data)]
             ["app/package.json" (render "app__package.json" data)]
             ["Gruntfile.js" (render "Gruntfile.js" data)]
             [".gitignore" (render ".gitignore" data)]
             ["package.json" (render "package.json" data)]
             ["project.clj" (render "reagent__project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["src/{{sanitized}}/core.cljs" (render "src__core.cljs" data)]
             ["src_front/{{sanitized}}/core.cljs" (render "reagent__src__core.cljs" data)]
             ["src_tools/figwheel_middleware.clj" (render "figwheel_middleware.clj" data)]
             "resources")))

(defn descjop
  "An Electron(atom-shell) application project template."
  [name & params]
  (cond
    (= name "help") (println help-text)
    (= (first params) "+om") (project-om name)
    (= (first params) "+reagent") (project-reagent name)
    :else (project-default name)))

(def help-text
  "help text for cli"
  "### (if you don't install grunt yet.)

```
$ npm install -g grunt-cli
```

## Usage

You can display help how to use descjop.

```
$ lein new descjop help
```

## Usage : Default project

```
$ lein new descjop YOUR_APP_NAME
```

see your app dir. looks like

```
.
+-- README.md
+-- app
|   +-- index.html // entry html file
|   +-- js
|   |   +-- cljsbuild-main.js // compiled JavaScript
|   |   +-- externs.js
|   |   +-- main.js
|   +-- package.json // for Desktop app
+-- package.json // for Compile
+-- project.clj // compile settings desktop app
+-- src
    +-- NAMESPACE
        +-- core.cljs // ClojureScript in here
```

### step 1

run npm command below.

```
$ npm install
$ grunt download-electron
 
Running \"download-electron\" task
 
Done, without errors.
```

### step 2

you have to change `./PROJECT_NAME/core.cljs` about `:companyName` and `submitURL`.

```
(defn -main []
  (.start crash-reporter (clj->js {:companyName \"Your Company Name\"
                                   :submitURL   \"http://example.com/\"}))
  ...
```

### step 3

and run extern commands,

```
$ lein externs > app/js/externs.js
```

run cljsbuild.

```
$ lein cljsbuild once

Compiling ClojureScript.
Compiling \"app/js/cljsbuild-main.js\" from [\"src\"]...
Successfully compiled \"app/js/cljsbuild-main.js\" in 10.812 seconds.
```

### step 4

you can run Electron(Atom-Shell) app.

On Windows:

```
$ .\\electron\\electron.exe app
```

On Linux:

```
$ ./electron/electron app
```

On OS X:

```
$ ./electron/Electron.app/Contents/MacOS/Electron app
```



## Usage : [Om](https://github.com/omcljs/om) based project

```
$ lein new descjop YOUR_APP_NAME +om
```

see your app dir. looks like

```
.
+-- README.md
+-- app
|   +-- index.html // entry html file
|   +-- js
|   |   +-- cljsbuild-main.js // compiled JavaScript
|   |   +-- externs.js
|   |   +-- main.js
|   +-- package.json // for Desktop app
+-- package.json // for Compile
+-- project.clj // compile settings desktop app
+-- src
|   +-- NAMESPACE
|       +-- core.cljs // ClojureScript for Electron in here
+-- src_front
    +--NAMESPACE_om
       +-- core.cljs // Frontend clojureScript in here
```

om project support `figwheel`.

```
$ lein trampoline figwheel frontend
```


### step 1

run npm command below.

```
$ npm install
$ grunt download-electron
 
Running \"download-electron\" task
 
Done, without errors.
```

### step 2

you have to change `./PROJECT_NAME/core.cljs` about `:companyName` and `submitURL`.

```
(defn -main []
  (.start crash-reporter (clj->js {:companyName \"Your Company Name\"
                                   :submitURL   \"http://example.com/\"}))
  ...
```

### step 3

and run extern commands,

```
$ lein externs > app/js/externs.js
```

run cljsbuild.

```
$ lein cljsbuild once

Compiling ClojureScript.
Compiling \"app/js/cljsbuild-main.js\" from [\"src\"]...
Successfully compiled \"app/js/cljsbuild-main.js\" in 10.812 seconds.
```

### step 4

om project support `figwheel`.

open other terminal window, and run.

```
$ lein trampoline figwheel frontend
```

### step 5

you can run Electron(Atom-Shell) app.

On Windows:

```
$ .\\electron\\electron.exe app
```

On Linux:

```
$ ./electron/electron app
```

On OS X:

```
$ ./electron/Electron.app/Contents/MacOS/Electron app
```


## Usage : [reagent](https://github.com/reagent-project/reagent) based project

```
$ lein new descjop YOUR_APP_NAME +reagent
```

see your app dir. looks like

```
.
+-- README.md
+-- app
|   +-- index.html // entry html file
|   +-- js
|   |   +-- cljsbuild-main.js // compiled JavaScript
|   |   +-- externs.js
|   |   +-- main.js
|   +-- package.json // for Desktop app
+-- package.json // for Compile
+-- project.clj // compile settings desktop app
+-- src
|   +-- NAMESPACE
|       +-- core.cljs // ClojureScript for Electron in here
+-- src_front
|    +--NAMESPACE
|       +-- core.cljs // Frontend clojureScript in here
+-- src_tools
     +-- figwheel_middleware.clj // figwheel helper
```

### step 1

run npm command below.

```
$ npm install
$ grunt download-electron
 
Running \"download-electron\" task
 
Done, without errors.
```

### step 2

you have to change `./PROJECT_NAME/core.cljs` about `:companyName` and `submitURL`.

```
(defn -main []
  (.start crash-reporter (clj->js {:companyName \"Your Company Name\"
                                   :submitURL   \"http://example.com/\"}))
  ...
```

### step 3

and run extern commands,

```
$ lein externs > app/js/externs.js
```

run cljsbuild.

```
$ lein cljsbuild once

Compiling ClojureScript.
Compiling \"app/js/cljsbuild-main.js\" from [\"src\"]...
Successfully compiled \"app/js/cljsbuild-main.js\" in 10.812 seconds.
```

### step 4

reagent project support `figwheel`.

open other terminal window, and run.

```
$ lein trampoline figwheel frontend
```

### step 5

you can run Electron(Atom-Shell) app.

On Windows:

```
$ .\\electron\\electron.exe app
```

On Linux:

```
$ ./electron/electron app
```

On OS X:

```
$ ./electron/Electron.app/Contents/MacOS/Electron app
```

")
