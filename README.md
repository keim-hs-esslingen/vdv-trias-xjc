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

- ***bindings.xml:*** The bare minimum to make the code generation work.
- ***bindings-java-time.xml:*** Extended customizations that make XML date-time formats be converted to java.time-Classes instead of the generally used XMLGregorianCalendar class.

Choose one of these files, put it into your *trias-xsd* directory and name it e.g. *"bindings.xml"*.

## 5. Generate Java classes using `xjc` command.

While being in the *trias-xsd* directory,execute the following command:

```
xjc -d generated -npa -no-header -b bindings.xml Trias.xsd
```

The `-npa` flag changes the generation behaviour for package level annotations. Please consult the *xjc* man page for details about it.

The `-no-header` flag suppresses the generation of a header comment in each file. This header comment includes a generation timestamp that makes the generated file less compatible with SCM tools like git. Use this argument to prevent the generation of such comments (including their timestamps).

If you want to have the header included, but not the timestamp, omit this argument in the `xjc` command and apply the next step after your code was generated with headers (and timestamps).

## 6. (optional) Remove generation timestamps

If you want to include the generation headers in your generated code files but also commit them to an SCM tool, you most probably still want to get rid of the generation timestamp in your files.

The reason you don't want the timestamps is that they are put into every file upon generation. If you change the generation parameters, either by tweaking the `xjc` command line args or the bindings customizations in *binding.xml*, you will get new java files of which you probably only want to commit those to your repo, that have actually changed in content.

But since they all contain a generation timestamp, all of them appear as modified to the SCM, making it a pain to track changes along their timeline.

To remove the timestamps from the headers, use the following command. It traverses the directory of generated java files and extracts the lines matching the timestamp pattern. This command is only available for linux users. If you have one for Windows, feel free to contribute.

```
find ./generated -name "*.java" -exec sed -i -e '/\/\/ Generated on: [0-9\.]* at [0-9:]* .*$/d' {} \;
```

