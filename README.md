# Neolab and Myscript Integration
>This Android app contains Integration Codes that ties both the Neo Lab SDK and the MyScript Interactive Ink SDK


Hey there Have you ever been on a client's project that requires you to spend weeks reading and understanding a documentation or some very strange API. very stressful right
and let's say it's not a common API and Google don't really have the answers you need. so you then have to go all the way yourself, making mistakes and fixing them yourselves, opening your own stackoverflow question (terrible right)
that sort of thing is why this repo was created.

It's an example app that could save you weeks in development.
It shows practical ways of integrating the [Neolab SDK](https://www.neolab.net) and the [MyScript](https://www.myscript.com/) into an android app

## NeoLab
The [Neolab SDK](https://www.neolab.net) SDK is a software created for use with the [NeoSmartPen](https://www.neosmartpen.com/) a digital pen used to collect data written on paper that contains the Special [Ncode](https://www.neolab.net/en/technology/#ncodetech) Pattern
The SDK which is available for not only android but other platforms too carries out the communication with the Pen in obtaining data (in form of strokes) gotten from the Pen

### MyScript
The [MyScript](https://www.myscript.com/) Interactive Ink SDK is an SDK that performs Interpretation of digital ink, most times in form of strokes that contains pointer to digital text.
So the myscript SDK takes in a set of strokes and through their algorithms, it outputs in digital text what a user wrote, Its Interpretation accuracty is Top Notch

### Our App
Our app here uses both Libraries, the app is a data collection app that connects with the NeoSmart Pen and collects Stoke Data with the Help of the NeoLab SDK and then interprets it to digital text using Myscript's SDK.

### Test Document
In the Project there is a Document Class and it's properties depends on certain coordinates of the live fields on the document that is being worked on in the project a particular document (A Manifest) was used and a sample of how the document looks like is in the root of the folder with name "sample_doc.pdf"
