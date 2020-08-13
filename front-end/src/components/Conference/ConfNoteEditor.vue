<template>
    <div class="editor">
      <!-- Upper Menu -->
      <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
        <div class="menubar">
          <button
            size="sm"
            class="menubar__button"
            :class="{ 'is-active': isActive.bold() }"
            @click="commands.bold"
          >
            <b-icon-type-bold v-b-tooltip.hover title="bold" font-scale="2"></b-icon-type-bold>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.italic() }"
            @click="commands.italic"
          >
            <b-icon-type-italic v-b-tooltip.hover title="Italic" font-scale="2"></b-icon-type-italic>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.strike() }"
            @click="commands.strike"
          >
            <b-icon-type-strikethrough v-b-tooltip.hover title="Strike" font-scale="2"></b-icon-type-strikethrough>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.underline() }"
            @click="commands.underline"
          >
            <b-icon-type-underline v-b-tooltip.hover title="Under line" font-scale="2"></b-icon-type-underline>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.code() }"
            @click="commands.code"
          >
            <b-icon-code v-b-tooltip.hover title="Code" font-scale="2"></b-icon-code>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.paragraph() }"
            @click="commands.paragraph"
          >
            <b-icon-pencil v-b-tooltip.hover title="Paragraph" font-scale="2"></b-icon-pencil>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.heading({ level: 1 }) }"
            @click="commands.heading({ level: 1 })"
          >
            <b-icon-type-H1 v-b-tooltip.hover title="Heading 1" font-scale="2"></b-icon-type-H1>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.heading({ level: 2 }) }"
            @click="commands.heading({ level: 2 })"
          >
            <b-icon-type-H2 v-b-tooltip.hover title="Heading 2" font-scale="2"></b-icon-type-H2>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.heading({ level: 3 }) }"
            @click="commands.heading({ level: 3 })"
          >
            <b-icon-type-H3 v-b-tooltip.hover title="Heading 3" font-scale="2"></b-icon-type-H3>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.bullet_list() }"
            @click="commands.bullet_list"
          >
            <b-icon-list-ul v-b-tooltip.hover title="Unordered List" font-scale="2"></b-icon-list-ul>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.ordered_list() }"
            @click="commands.ordered_list"
          >
            <b-icon-list-ol v-b-tooltip.hover title="Ordered List" font-scale="2"></b-icon-list-ol>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.blockquote() }"
            @click="commands.blockquote"
          >
            <b-icon-blockquote-left v-b-tooltip.hover title="Block Quote" font-scale="2"></b-icon-blockquote-left>
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.code_block() }"
            @click="commands.code_block"
          >
            <b-icon-file-code v-b-tooltip.hover title="Code Block" font-scale="2"></b-icon-file-code>
          </button>

          <button class="menubar__button" @click="commands.horizontal_rule">
            <b-icon-hr v-b-tooltip.hover title="Br" font-scale="2"></b-icon-hr>
          </button>

          <button class="menubar__button" @click="commands.undo">
            <b-icon-arrow90deg-left v-b-tooltip.hover title="Undo" font-scale="2"></b-icon-arrow90deg-left>
          </button>

          <button class="menubar__button" @click="commands.redo">
            <b-icon-arrow90deg-right v-b-tooltip.hover title="Redo" font-scale="2"></b-icon-arrow90deg-right>
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
      <b-form inline>
          <b-form-input
            v-model="noteObj.title"
            type="text"
            required
            placeholder="Note Title"
          ></b-form-input>
          <!-- <b-button class="menubar__button save_button" @click="SaveNote" variant="primary">Save</b-button>   -->
          <b-button v-if="!isSave" class="menubar__button save_button" @click="SaveNote" variant="outline-primary">Save</b-button>
          <b-button v-if="isSave" class="menubar__button save_button" @click="EditNote" variant="outline-success">Edit</b-button>


      </b-form>
      <hr>
      <div @click="focusNote" class="border border-secondary rounded">
        <editor-content class="editor__content scroll" :editor="editor" />
      </div>
    </div>
</template>

<script>
import SERVER from '../../api/spring.js';
import axios from 'axios';
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
    meetingInfo: Object
  },
  data() {
    return {
      isSave: false,
      groupNo: this.meetingInfo.groupNo,
      meetingNo: this.meetingInfo.meetingNo,
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
          this.noteObj.content = getHTML();
        },
      }),
      noteObj:{
        content:'',
        title:'',
        id: null,
      }
    };
  },
  methods: {
    focusNote() {
      this.editor.focus();
    },
    EditNote() {
      const URL_saveNote = '/note/';
            
      axios.put(SERVER.URL + URL_saveNote + this.noteObj.id,{
        'title': this.noteObj.title,
        'content': this.noteObj.content
      }).then((res)=>{
        console.log('title:', this.noteObj.title);
        console.log('Content:', this.noteObj.content);
      }).catch((err)=> console.error(err));

    },
    SaveNote(){      
      // 없으면 1을 넣는다. 임시용.
      // if (this.meetingId === undefined && this.groupId === undefined){
      //   this.groupId = 1;
      //   this.meetingId = 1;
      // }
      const note = {content: this.noteObj.content, groupNo: this.groupNo, id: this.$store.state.userId, meetingNo:this.meetingNo, title: this.noteObj.title};
      console.log(note);
      axios.post(SERVER.URL + '/note/save', note)
        .then(res=>{
          console.log(res);
          this.noteObj.id = res.data.noteNo;
          this.isSave = true;
        })
        .catch(err=>console.error(err));
    }
  },
  beforeDestroy() {
    this.editor.destroy();
  },
};
</script>

<style scoped>
.menubar__button {
  font-size: 80% !important;
}
.editor {
  padding: 0.5rem !important;
}
.editor__content{
    height: 30rem;
}
.save_button{
  margin: 0 0  0 auto;
}
hr {
  margin: 0.5rem auto;
}
</style>