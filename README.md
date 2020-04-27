# vdv-trias-xjc

This is a small guide on how to use the `xjc` tool to generate Java classes from the VDV Trias XML schema. It applies to the Trias schema v1.2, which can be downloaded from the VDV-Website here: https://www.vdv.de/trias-xsd-v1.2.zipx

Whether this procedure can be used for Trias schemas of other versions has not been tested.

The commands written in this document were used on a Ubuntu machine. On Windows and other platforms, they probably look different, but when it comes to the actual `xjc` tool, they should be quite similar, if not identical.

## 1. Download the XML-Schema

You get it here: https://www.vdv.de/trias-xsd-v1.2.zipx


## 2. Extract schema

Extract the schema to folder *"trias-xsd"* (or whatever else you want to name it)

```
unzip trias-xsd-v1.2 -d trias-xsd
```

## 3. Prepare directories

Enter the directory to where the schema was extracted.

```
cd trias-xsd
```

Make a target folder inside this directory.
This is where the java classes will be put into.
Omitting this step will let the `xjc` command fail.

```
mkdir generated
```

## 4. Add JAXB-Binding-Customizations file

For the class generation to work, you will need external [JAXB-Binding-Customizations](https://docs.oracle.com/cd/E17802_01/webservices/webservices/docs/1.5/tutorial/doc/JAXBUsing4.html).

In this repository you will find two external binding customization files, of which you can choose one that fits your situation.

- *bindings.xml*: The bare minimum to make the code generation work.
- *bindings-java-time.xml*: Extended customizations that make XML date-time formats be converted to java.time-Classes instead of the generally used XMLGregorianCalendar class.

Choose one of these files, put it into your *trias-xsd* directory and name it e.g. *"bindings.xml"*.

## 5. Generate Java classes using `xjc` command.

While being in the *trias-xsd* directory,execute the following command:

```
xjc -d generated -npa -b bindings.xml Trias.xsd
```

The `-npa` flags are needed for JAXB unmarshallers to correctly deserialize XML.


## 6. Remove generation timestamps (optional for git, svn, ... users)

Once you generated the java classes and put them in a code repo, commited and pushed them, you might realize afterwards that you need to tweak the generated classes a bit, e.g. by editing the *bindings.xml* file or the `xjc` command arguments. This will cause you to regenerate the files, put the new version into you repo and so forth. This might even be necessary a couple times until the generated classes fit you needs perfectly.

However, everytime you generate classes with `xcj` a generation timestamp is put into every generated code file, making all of them appear as modified to SCM tools, even if the rest of the file hasn't changed. Most of the time though, you only want to commit those files that have changed their content due to the modifications done to *bindings.xml*.

To make this possible, the following command can be used to traverse the directory of generated classes and remove the generation timestamp from every *\*.java* file found in there. On windows this command will probably look different.

```
find ./generated -name "*.java" -exec sed -i -e '/\/\/ Generated on: [0-9\.]* at [0-9:]* .*$/d' {} \;
```

