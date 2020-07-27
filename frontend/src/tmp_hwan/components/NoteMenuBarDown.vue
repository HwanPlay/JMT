<template>
  <div>

    <textarea id="text"> 
             Welcome to GeeksforGeeks 
         </textarea>
    <br />
    <input type="button" id="btn" value="Download" />

    <button id="btn" value="Download">Download</button>
  </div>
</template>

<script>
export default {
  name: "NoteDown",
  props: {
    content: String,
  },
  mounted:{
      
  }
};
// function download(file, text) {
//   //creating an invisible element
//   const element = document.createElement("a");
//   element.setAttribute(
//     "href",
//     "data:text/plain;charset=utf-8, " + encodeURIComponent(text)
//   );
//   element.setAttribute("download", file);

//   // Above code is equivalent to
//   // <a href="path of file" download="file name">

//   document.body.appendChild(element);

//   //onClick property
//   element.click();

//   document.body.removeChild(element);
// }

// // Start file download.
// document.getElementById("btn").addEventListener(
//   "click",
//   function () {
//     // Generate download of hello.txt
//     // file with some content
//     const text = document.getElementById("text").value;
//     const filename = "GFG.txt";

//     download(filename, text);
//   },
//   false
// );
</script>

<style>
</style>