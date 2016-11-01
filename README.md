# WikiWeKey
Project Repo for team WikiWeKey


## Description
A wiki markup interpreter, editor, and GUI in the wiki markdown language. For the 2015 summer 1 term of 
Managing Software Development (begining of May to end of June)

# NOTE
There are many wiki interperters already out there that are much better than this one and I see no reason to reinvent the wheel. For that reason I won't be updating this project and recomend you use one of the other ones if you came here looking for a wiki interpreter. Otherwise, it's a respectable given that it was a part time effort of three CS students over less than 2 months. It work well but lacks a few features. These and known bugs are documented below. Feel free to play with it or build upon the code as you like. 

# Scrum Board
https://trello.com/b/qEACcDDA

### 1. Contact Information:
**Name:** Tim Cowley **Email:**  timothycowley@gmail.com   
**Name:** Sneha Nagrikar **Email:**   ...
**Name:** Hu Po-An   **Email:**   ...

----
### 2. Links to Materials:
Both source codes and design notes folders have been uploaded to the same gibhub repository under:  
[WikiWeKey Repository](https://github.ccs.neu.edu/cs5500s15-seattle/WikiWeKey)  
In the repository, design notes are under DesignNotes folder and source codes are under  
WikiWeKeyProjectMavenV2 folder


----
### 3. Detail instructions on how to:
1. check out source code

To clone repo to your machine use following commands
git clone https://github.ccs.neu.edu/cs5500s15-seattle/WikiWeKey.git

2. build source code

    Source code can be further built on by using any Java IDE which can access the classes in source code directly. However, user is recommended to use IDE to import the exiting Maven project which WikiWeKey is built upon. Eclipse is recommended to use. 
    
    Steps to set up Eclipse is showned in the following website: https://ist.berkeley.edu/as-ag/tools/howto/install-eclipse-win.html
    
    To create a maven project in eclipse

    Once Eclipse and Java JDK are properly installed, user can import the source code as a Maven project by
    
    1. Click on File and select import.
    2. Select Maven folder and choose Exisint Maven Projects and click on next.
    3. Locate the directory of the source code folder in the local machine.
    4. Click on Advanced to specify the name of the project for IDE.
    5. Check the box under Projects and click on Finish.
    
3. run all tests (unit and integration tests)

    **Note:** During testing and report generation, some UI screens will pop up, please make sure that all UI boxes are checked so that the report will complete its build. 

    Tests are available under \WikiWeKeyProjectMavenV2\src\test\java folder. Every class has its own class for testing. \(except for Page classes\). 

4. generate all reports/documentation from source code

    As the proejct is built using Maven project, test reports, coverage reportes as well as findBugs report can be generated using Maven plugins. The steps are as followed:
    
    1. on command line
    2. type in cd followed by directory of the folder WikiWeKeyProjectMavenV2
    3. run "mvn site"
    4. go to "target folder" to see the reports generated.
    

    
____
### 4. Detail instruction on how to run the program
We are presenting our Wiki product in the form of executable jar which saves wiki notes
in the local machine and therefore requires no internet access and wiki files created 
are easily accessible by the users to browse, edit and delete using machine's file system.

If you do not already have a WikiWeKey executable jar,
User can generate the executable jar using IDE tools and our recommendation is to use Eclipse IDE.

Once you have gotten the source codes as a maven project in Eclipse, you can turn the source code  
into executable jar by:

1. Right click on the project under Package Explorer and click on Export.
2. Select the type to export as Runable JAR file under Java folder, click next.
3. Under "Lauch Configuration" select the UIFrame class under the project name you specified.
4. Choose a location you wish to run this product  
    **Note:** Once the product is launched, it will create a folder called WikiNotes to
    store Wiki notes if the folder is not already existed under the current directory of the executable.
    If there is such a folder under the directory before the program is run, Wiki notes will be saved 
    in the existing WikiNotes folder.
5. Select Extract required libraries into JAR and click on finish.
6. An executable jar should appear in the location you specified. Dobcle click on it to launch.

or you can choose to create a runnable JAR file using commmand prompt

Creating a jar File in Command Prompt

1. Start Command Prompt.
    Navigate to the folder that holds your class files:
    C:\>cd \mywork

2. Set path to include JDK’s bin.  For example:

   C:\mywork> path c:\Program Files\Java\jdk1.7.0_25\bin;%path%
   
3. Compile your class(es):
    C:\mywork> javac *.java

4. Create a manifest file and your jar file:
    C:\mywork> echo Main-Class: Craps >manifest.txt
    C:\mywork> jar cvfm Craps.jar manifest.txt *.class  
    or  
    C:\mywork> jar cvfe Craps.jar Craps *.class

5. Test your jar:

    C:\mywork> Craps.jar  
    or  
    C:\mywork> java -jar Craps.jar


----
### 5. A list of any bugs/limitations that you know are present/valid in your code
#### Bugs
1. Emphasis Marks: In certain conditions Bold marks do not become italic marks when they should, such as with "\*\*text\*"

2. A bug appears where \<s>text\<strong>text\</s>text\</strong> renders as ~~text**text**~~text. It closes the bold on the strike end. Testing with the JEditorPanel indicates that this is typical behavior of the JEditorPanel so we believe the source of the bug is actually in the swing component and not the code we wrote. Interestingly, this behavior also appears when I type it here, on GitHub: <s>text<strong>text</s>text</strong> but does not appear when I create an html file myself. Perhaps there are different sets of rules for rendering HTML. 

3. If a linebreak occurs at a bulleted element of a list, the following lines get wrapped in a paragraph 
         * list start  \r\n
         text
         
         becomes 
         <ul><li>list start 
         <p>text</p></li></ul>
         should be
         <ul><li>list text</li></ul>


3. **IMPORTANT:** a bug occasionally occures in preview preventing text from being interpreted. Text will still appear, but not as properly formated markdown. Saving the file or restarting corrects this bug. In testing this occured most often when the entire content in the editor panel was deleted before adding new content; however the bug does not occur consistently enough to determin with certainty how it is triggered. 

#### Deviations from behavior of Markdown as specified on <http://daringfireball.net/projects/markdown> or as determined by testing behavior on that sites interpreter

1. Block Quote behavior: In our implementation a block quote ends when a blank line is encountered. Daringfireball implementation allows blank lines in block quotes as long as the first text line after the blank lines starts with a ">". Daringfireball uses the blank line to seperate paragraphs within the block quote. In this situation our implementation generates two blockquotes. When rendered this looks similar (as seen below), but the underlying HTML is different.

<h6> our implementation </h6>
<blockquote><p> line 1 </p> </blockquote>
<blockquote><p> line 2 </p> </blockquote>

<h6> daringfireball </h6>
<blockquote><p> line 1 </p>
            <p> line 2 </p> </blockquote>

2. List behavior: In our implementation a list ends when a blank line is encountered. Daringfireball implementation allows blank lines in lists as long as the first line with text after the blank line starts with a list bullet mark. In such a situation our implementation instead generates two lists. When rendered this looks similar, except for nested lists. If the list element after the blank line is indented further Daringfireball will make it a nested list, while our interpratation starts it at the first level. 
    <ul><li>our space within nested list</li></ul>
    <ul><li>renders two unnested lists </li></ul>
    <ul><li><p>daringfireball space within nested list</p></li>
        <ul><li><p>renders as nested</p></li></ul></ul>
  

    We did not implement the feature that allows adding additional paragraphs to a list element by indenting 4 spaces. 
    
    Daringfireball limits list nesting to three levels total, our implementation does not put a bound on how deep the nesting may go. 
    
3. Link behavior: In our implementation markdown marks within a link are not rendered, in Daringfireball implementation marks are rendered in the text shown for the link.

    Links are marked by "[...]\(...)". In our implementation if there are multiple "[" the one closest to the "\](" is the one utilized. Daringfireball does the same unless the inner "[" has a matching "]" in which case it uses the outer "[". That is in "[...[...]...\](...)" the first "[" is utilized, while in our implementation the 2nd "[" is utilized.
    
    It was not required to allow titles, so this feature was not implemented

4. Automatic Link behavior: Daringfireball does not specify how automatic links are determined to be valid, but testing suggests that it checks for the presence of "http://" or "https://". Our code checks for the presence of "http".

5. Automatic Escapes: The document only specifies that "&" and sometimes "<" are automatically escaped, but other characters may also be escaped that aren't mentioned. We only escape these two. Testing indicates that Daringfireball escapes "<" when it comes before whitespace or another "<", otherwise left unchanged. Ours escapes "<" when it comes before whitespace, an html header or an html paragraph tag. We plan on changing this behavior to match Daringfireball.

6. Emphasis behavior:     Our code allows nesting of Bolds indicated by ** and by __ such that \*\*text \_\_text\_\_ text\*\* is interpreted as <strong>text <strong>text</strong> text</strong> but daringfireball interpretes it as <strong>text <em>_text</em>_ text</strong> Note: In this case the behavior of our code is the same as that of Github Markdown.

7. Markdown specification does not tell what to do if a line starts with "#" but has no other text. Our code makes this a header with no text. Daringfireball leaves the "#" uninterpreted.  

8. Our interpreter does not allow code blocks within lists or block quotes


#### Not implemented
1. code (not sure if required)
2. backslash escapes (not sure if required)
3. images (not required)
4. in line HTML (not required)

### 6. A list of any extensions that you have implemented on top of the basic feature set
1. User can choose to save his or her wiki note under a sub-folder of the WikiNotes folder by specifying the file to contain the wiki note. 
 
    For example, if user would like to save wiki note **"SingleTon Pattern"** under directory, **\WikiNotes\Design Patterns**, he/she can type in **"Design Patterns\SingleTon Pattern"** as the name of the wiki note when saving.

2. When searching, wiki notes having the same name but under different directory will all be displayed with the path of the sub-folders to WikiNotes folder be attached to the front of the wiki note.

    For example, if user have two wiki notes called **"singleTon pattern"** under different directory. One under **\WikiNotes\Good Patterns\**, another one under **\WikiNotes\Design Patterns** and the last one under **\WikiNotes**. When SingleTon Pattern is searched, results will be displayed as:
    1. SingleTon Pattern
    2. Design Patterns\SingleTon Pattern
    3. Good Patterns\SingleTon Pattern

