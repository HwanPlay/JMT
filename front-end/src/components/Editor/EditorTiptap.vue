<template>
    <div class="editor editor-container">
      <!-- Upper Menu -->
   <editor-menu-bar :editor="editor" v-slot="{ commands, isActive, focused }">
        <div class="menubar is-hidden" :class="{'is-focused' : focused}">
          <button v-if="false" class="menubar__button">
            <!-- @click="downHTMLDocument" -->
            <b-icon-download v-b-tooltip.hover title="Download File" font-scale=fontScale></b-icon-download>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.bold() }"
            @click="commands.bold"
          >
            <b-icon-type-bold v-b-tooltip.hover title="bold" font-scale=fonsScale></b-icon-type-bold>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.italic() }"
            @click="commands.italic"
          >
            <b-icon-type-italic v-b-tooltip.hover title="Italic" font-scale=fontScale></b-icon-type-italic>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.strike() }"
            @click="commands.strike"
          >
            <b-icon-type-strikethrough v-b-tooltip.hover title="Strike" font-scale=fontScale></b-icon-type-strikethrough>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.underline() }"
            @click="commands.underline"
          >
            <b-icon-type-underline v-b-tooltip.hover title="Under line" font-scale=fontScale></b-icon-type-underline>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.code() }"
            @click="commands.code"
          >
            <b-icon-code v-b-tooltip.hover title="Code" font-scale=fontScale></b-icon-code>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.paragraph() }"
            @click="commands.paragraph"
          >
            <b-icon-pencil v-b-tooltip.hover title="Paragraph" font-scale=fontScale></b-icon-pencil>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.heading({ level: 1 }) }"
            @click="commands.heading({ level: 1 })"
          >
            <b-icon-type-H1 v-b-tooltip.hover title="Heading 1" font-scale=fontScale></b-icon-type-H1>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.heading({ level: 2 }) }"
            @click="commands.heading({ level: 2 })"
          >
            <b-icon-type-H2 v-b-tooltip.hover title="Heading 2" font-scale=fontScale></b-icon-type-H2>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.heading({ level: 3 }) }"
            @click="commands.heading({ level: 3 })"
          >
            <b-icon-type-H3 v-b-tooltip.hover title="Heading 3" font-scale=fontScale></b-icon-type-H3>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.bullet_list() }"
            @click="commands.bullet_list"
          >
            <b-icon-list-ul v-b-tooltip.hover title="Unordered List" font-scale=fontScale></b-icon-list-ul>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.ordered_list() }"
            @click="commands.ordered_list"
          >
            <b-icon-list-ol v-b-tooltip.hover title="Ordered List" font-scale=fontScale></b-icon-list-ol>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.blockquote() }"
            @click="commands.blockquote"
          >
            <b-icon-blockquote-left v-b-tooltip.hover title="Block Quote" font-scale=fontScale></b-icon-blockquote-left>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.code_block() }"
            @click="commands.code_block"
          >
            <b-icon-file-code v-b-tooltip.hover title="Code Block" font-scale=fontScale></b-icon-file-code>
          </button>

          <button class="menubar__button" @click="commands.horizontal_rule">
            <b-icon-hr v-b-tooltip.hover title="Br" font-scale=fontScale></b-icon-hr>
          </button>

          <button class="menubar__button" @click="commands.undo">
            <b-icon-arrow90deg-left v-b-tooltip.hover title="Undo" font-scale=fontScale></b-icon-arrow90deg-left>
          </button>

          <button class="menubar__button" @click="commands.redo">
            <b-icon-arrow90deg-right v-b-tooltip.hover title="Redo" font-scale=fontScale></b-icon-arrow90deg-right>
          </button>

          <!-- <button class="menubar__button" @click="editNoteHTML">
            <h4>
              <b-badge variant="primary">Edit Content</b-badge>
            </h4>
          </button> -->
        </div>
      </editor-menu-bar>

      <!-- Bubble Menu -->
      <editor-menu-bubble
        :editor="editor"
        :keep-in-bounds="keepInBounds"
        v-slot="{ commands, isActive, menu }"
      >
        <div
          class="menububble"
          :class="{ 'is-active': menu.isActive }"
          :style="`left: ${menu.left}px; bottom: ${menu.bottom}px;`"
        >
          <button
            class="menububble__button"
            :class="{ 'is-active': isActive.bold() }"
            @click="commands.bold"
          >
            <b-icon-type-bold class="h5 border rounded"></b-icon-type-bold>
          </button>

          <button
            class="menububble__button"
            :class="{ 'is-active': isActive.italic() }"
            @click="commands.italic"
          >
            <b-icon-type-italic class="h5 border rounded"></b-icon-type-italic>
          </button>

          <button
            class="menububble__button"
            :class="{ 'is-active': isActive.code() }"
            @click="commands.code"
          >
            <b-icon-code class="h5 border rounded"></b-icon-code>
          </button>
        </div>
      </editor-menu-bubble>

      <!-- {{dataHTML}} -->
      <div class="d-flex justify-space-around">
        <input 
          type="text" 
          maxlength = "18"
          class="title-form form-control" 
          v-model="dataNoteObj.Title" 
          required
          placeholder="Note Title">
          
          <div class="note_button">

            <!-- <b-button class="mx-1" @click="editNoteTitle" variant="primary">Edit Title</b-button>
            <b-button class="mx-1" @click="editNoteContent" variant="primary">Edit Content</b-button> -->
            
              <v-tooltip v-model="show" top>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn  class="mx-2" :elevation="3" fab dark color="blue" @click="saveNote" v-bind="attrs" v-on="on">
                     <v-icon style="color:white">mdi-pencil</v-icon>
                  </v-btn>
                </template>
                <span style="font-size:12px;">EDIT</span>
              </v-tooltip>

              <v-tooltip v-model="show" top>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn  class="mx-2" :elevation="3" fab dark color="#EF5350" @click="deleteNote" v-bind="attrs" v-on="on">
                     <i class="fas fa-trash-alt" style="color:white" ></i>
                  </v-btn>
                </template>
                <span style="font-size:12px;">DELTE</span>
              </v-tooltip>
            
          </div>
      </div>
      <hr>
      <div @click="focusNote">
        <editor-content class="editor__content scroll" :editor="editor" />
      </div>
      
    </div>
</template>

<script>
import { Editor, EditorContent, EditorMenuBar, EditorMenuBubble } from 'tiptap';
import {
  Blockquote,
  CodeBlock,
  HardBreak,
  Heading,
  HorizontalRule,
  OrderedList,
  BulletList,
  ListItem,
  TodoItem,
  TodoList,
  Bold,
  Code,
  Italic,
  Link,
  Strike,
  Underline,
  History,
} from 'tiptap-extensions';
export default {
  
  components: {
    EditorContent,
    EditorMenuBar,
    EditorMenuBubble,
  },
  props: {
    noteObj: Object,
  },
  data() {
    return {
      fontScale: 1.5,
      
      keepInBounds: true,
      editor: new Editor({
        extensions: [
          new Blockquote(),
          new BulletList(),
          new CodeBlock(),
          new HardBreak(),
          new Heading({ levels: [1, 2, 3] }),
          new HorizontalRule(),
          new ListItem(),
          new OrderedList(),
          new TodoItem(),
          new TodoList(),
          new Link(),
          new Bold(),
          new Code(),
          new Italic(),
          new Strike(),
          new Underline(),
          new History(),
        ],
        content: '',
        onUpdate: ({ getHTML }) => {
          this.dataNoteObj.Content = getHTML();
        },
      }),
      example_data: ''
      ,
      dataNoteObj:{
        Content: '',
        Title: '',
        Id: 0,
        groupNo: 0,
      },
    };
  },
  watch: {
    noteObj: {
      deep: true,
      handler() {
        this.dataNoteObj.Content = this.noteObj.content;
        this.dataNoteObj.Title = this.noteObj.note_title;
        this.dataNoteObj.Id = this.noteObj.noteNo;
        this.dataNoteObj.groupNo = this.noteObj.groupNo;
        this.editor.setContent(this.dataNoteObj.Content);
        this.editor.focus();
      }
    },
  },
  methods: {
    focusNote() {
      this.editor.focus();
    },
    saveNote() {
      console.log('SaveNote');
      this.$emit('onSaveNote', this.dataNoteObj);
      this.editor.focus();
    },
    deleteNote() {
      this.$emit('onDeleteNote', {noteNo:this.dataNoteObj.Id, groupNo: this.dataNoteObj.groupNo });
    },
  },
  beforeDestroy() {
    this.editor.destroy();
  },
  
};
</script>
<style scoped>

hr{
  height: 1.5px;
  background:#dfdfdf;
  margin : 15px 10px 10px 15px;
}

.menubar__button {
  margin-left: 10px;
  margin-top : 15px;
}
.editor__content{
  font-size: 18px;
  padding: 0.9rem;
  height: 60.5vh;
}

.note_button{
  margin: 0 0 0 auto;
}

.title-form {
  width: 28rem;
  border: 0px;
  font-weight: 600;
  font-size: 33px;
  font-family: "NanumSquare", sans-serif;
  padding: 3px 13px;
  height: calc(1.0em + 0.75rem + 2px);
}
.title-form:focus {
  border-color: inherit;
  -webkit-box-shadow: none;
  box-shadow: none;
}
.noteBtn{
  font-size: 14px;
}

.editor-container{
  margin-left: 70px;
  width: 65%;
}
</style>