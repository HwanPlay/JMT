<template>
  <b-col cols="7">
    <div class="editor">
      <!-- Upper Menu -->
      <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
        <div class="menubar">
          <button v-if="false" class="menubar__button">
            <!-- @click="downHTMLDocument" -->
            <b-icon-download v-b-tooltip.hover title="Download File" font-scale="2"></b-icon-download>
          </button>

          <button
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
            v-model="dataNoteObj.Title"
            type="text"
            required
            placeholder="Note Title"
          ></b-form-input>

          
          <div class="note_button">
            <b-button class="mx-1" @click="saveNote" variant="outline-primary">Save</b-button>

            <!-- <b-button class="mx-1" @click="editNoteTitle" variant="primary">Edit Title</b-button>
            <b-button class="mx-1" @click="editNoteContent" variant="primary">Edit Content</b-button> -->
            <b-button class="mx-1" @click="deleteNote" variant="outline-danger">Del Note</b-button>
          </div>
      </b-form>
      <hr>
      <div @click="focusNote" class="border border-secondary rounded">
        <editor-content class="editor__content scroll" :editor="editor" />
      </div>
    </div>
  </b-col>
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
      }
    };
  },
  watch: {
    noteObj: {
      deep: true,
      handler() {
        this.dataNoteObj.Content = this.noteObj.Content;
        this.dataNoteObj.Title = this.noteObj.Title;
        this.dataNoteObj.Id = this.noteObj.Id;
        this.editor.setContent(this.dataNoteObj.Content);
        this.editor.focus();
      }
    }
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
      this.$emit('onDeleteNote', this.dataNoteObj.Id);
      this.dataNoteObj.Title = 'Select Note';
      this.editor.setContent(this.example_data);
      this.dataNoteObj.Id = 0;
    }
  },
  beforeDestroy() {
    this.editor.destroy();
  },
};
</script>
<style scoped>
.note_button{
  margin: 0 0 0 auto;
}
</style>