<template>
  <v-container>
      <v-row>
        <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
            <div >
                <v-btn text icon
                :class="{ 'is-active': isActive.heading({ level: 1 }) }"
                @click="commands.heading({level: 1})"
                >
                   <b> H1 </b>
                </v-btn>
                <v-btn text icon
                :class="{ 'is-active': isActive.bold() }"
                @click="commands.bold"
                >
                    <v-icon>mdi-format-bold</v-icon>
                </v-btn>

                <v-btn text icon
                :class="{ 'is-active': isActive.underline() }"
                @click="commands.underline"
                >
                    <v-icon>mdi-format-underline</v-icon>
                </v-btn>

               <v-btn text icon
               @click="loadImage(commands.image)">
                   <v-icon>mdi-image</v-icon>
               </v-btn>
            </div>            
        </editor-menu-bar>
    </v-row>
    <v-row>
        <v-col cols=12 >
            <editor-content class="editor-box" :editor="editor"/>
        </v-col>
    </v-row>
  </v-container>
</template>


<script>
import { Editor, EditorContent, EditorMenuBar  } from 'tiptap';
import { Heading, 
        Bold, 
        Underline,
        Image } from 'tiptap-extensions';
export default {
components: {
    EditorContent,
    EditorMenuBar,
  },
  data() {
    return {
      editor: new Editor({
        content: `Type here...
        `,
        extensions:[
            new Heading({levels: [1,2,3]}),
            new Bold(),
            new Underline(),
            new Image(),
        ]
      })
    }
  },
  methods:{
      loadImage:function(command){
          command({src: "https://66.media.tumblr.com/dcd3d24b79d78a3ee0f9192246e727f1/tumblr_o00xgqMhPM1qak053o1_400.gif"})
      }
  },
  beforeDestroy() {
    this.editor.destroy()
  },
};
</script>
<style >
.editor-box> * {
    border-color: grey;
    border-style: solid;
    border-width: 1px;
}

.is-active{
    border-color: grey;
    border-style: solid;
    border-width: 1px;
}
 /* *:focus {
    outline: none;
}  */
</style>