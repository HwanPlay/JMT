<template>
  <div class="editor" style="margin-top: 100px;">
    <!-- Upper Menu -->
    <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
      <div class="menubar">
        <button class="menubar__button" @click="downHTMLDocument">
          <b-icon-download v-b-tooltip.hover title="File Down" class="h3 mb-2 border rounded"></b-icon-download>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bold() }"
          @click="commands.bold"
        >
          <b-icon-type-bold v-b-tooltip.hover title="Bold" class="h3 mb-2 border rounded"></b-icon-type-bold>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.italic() }"
          @click="commands.italic"
        >
          <b-icon-type-italic v-b-tooltip.hover title="Italic" class="h3 mb-2 border rounded"></b-icon-type-italic>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.strike() }"
          @click="commands.strike"
        >
          <b-icon-type-strikethrough
            v-b-tooltip.hover
            title="Strike"
            class="h3 mb-2 border rounded"
          ></b-icon-type-strikethrough>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.underline() }"
          @click="commands.underline"
        >
          <b-icon-type-underline
            v-b-tooltip.hover
            title="Under line"
            class="h3 mb-2 border rounded"
          ></b-icon-type-underline>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code() }"
          @click="commands.code"
        >
          <b-icon-code v-b-tooltip.hover title="Code" class="h3 mb-2 border rounded"></b-icon-code>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.paragraph() }"
          @click="commands.paragraph"
        >
          <b-icon-pencil v-b-tooltip.hover title="Paragraph" class="h3 mb-2 border rounded"></b-icon-pencil>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 1 }) }"
          @click="commands.heading({ level: 1 })"
        >
          <b-icon-type-H1 v-b-tooltip.hover title="Heading 1" class="h3 mb-2 border rounded"></b-icon-type-H1>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 2 }) }"
          @click="commands.heading({ level: 2 })"
        >
          <b-icon-type-H2 v-b-tooltip.hover title="Heading 2" class="h3 mb-2 border rounded"></b-icon-type-H2>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 3 }) }"
          @click="commands.heading({ level: 3 })"
        >
          <b-icon-type-H3 v-b-tooltip.hover title="Heading 3" class="h3 mb-2 border rounded"></b-icon-type-H3>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bullet_list() }"
          @click="commands.bullet_list"
        >
          <b-icon-list-ul v-b-tooltip.hover title="Unordered List" class="h3 mb-2 border rounded"></b-icon-list-ul>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.ordered_list() }"
          @click="commands.ordered_list"
        >
          <b-icon-list-ol v-b-tooltip.hover title="Ordered List" class="h3 mb-2 border rounded"></b-icon-list-ol>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.blockquote() }"
          @click="commands.blockquote"
        >
          <b-icon-blockquote-left
            v-b-tooltip.hover
            title="Block Quote"
            class="h3 mb-2 border rounded"
          ></b-icon-blockquote-left>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code_block() }"
          @click="commands.code_block"
        >
          <b-icon-file-code v-b-tooltip.hover title="Code Block" class="h3 mb-2 border rounded"></b-icon-file-code>
        </button>

        <button class="menubar__button" @click="commands.horizontal_rule">
          <b-icon-hr v-b-tooltip.hover title="Br" class="h3 mb-2 border rounded"></b-icon-hr>
        </button>

        <button class="menubar__button" @click="commands.undo">
          <b-icon-arrow90deg-left v-b-tooltip.hover title="Undo" class="h3 mb-2 border rounded"></b-icon-arrow90deg-left>
        </button>

        <button class="menubar__button" @click="commands.redo">
          <b-icon-arrow90deg-right v-b-tooltip.hover title="Redo" class="h3 mb-2 border rounded"></b-icon-arrow90deg-right>
        </button>

        <NoteMenuBarFileList />
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

    <label for="titleBox">Title:</label>
    <input class="my-1" type="text" id="titleBox" v-model="htmlTitle" />

    <div id="capture" class="content_box border border-secondary rounded">
      <editor-content  class="editor__content scroll" :editor="editor" />
    </div>
  </div>
</template>


<script>
// import NoteContent from "../components/NoteContent.vue";
// import NoteDown from "../components/NoteDown.vue";
import NoteMenuBarFileList from "../components/NoteMenuBarFileList.vue";
// import jsPDF from "jspdf";
// import html2canvas from "html2canvas";
import { saveAs } from "file-saver";

import { Editor, EditorContent, EditorMenuBar, EditorMenuBubble } from "tiptap";
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
} from "tiptap-extensions";
export default {
  components: {
    EditorContent,
    EditorMenuBar,
    EditorMenuBubble,
    NoteMenuBarFileList,
    // NoteContent,
    // NoteDown,
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
        content: `
          <h2>
            Hi there,
          </h2>
          <p>
            this is a very <em>basic</em> example of tiptap.
          </p>
          <pre><code>body { display: none; }</code></pre>
          <ul>
            <li>
              A regular list
            </li>
            <li>
              With regular items
            </li>
          </ul>
          <blockquote>
            It's amazing 👏
            <br />
            – mom
          </blockquote>
        `,
        onUpdate: ({ getHTML }) => {
          this.htmlText = getHTML();
        },
      }),
      htmlTitle: "제목",
      htmlText: "",
      };
  },
  beforeDestroy() {
    this.editor.destroy();
  },
  methods: {
    downHTMLDocument() {

      // document.querySelector('#capture').lastChild.lastChild.innerHTML
        const content = this.htmlText;
        const filename = this.htmlTitle+".html";
        const blob = new Blob([content], {
          type: "text/plain;charset=utf-8",
        });
        saveAs(blob, filename);
    },
  },
  mounted() {
    // document.querySelector('#capture').lastChild.lastChild.innerHTML = "<h2>Hi</h2>"
    // this.editor.content = 'hi'
  }
};
</script>

<style>
.fixed {
  position: sticky;
  top: 60px;
  padding: 10px 0px;
}
.scroll-box {
  overflow: scroll;
  width: 100%;
  height: 20em;
}
</style>

<style lang="scss">
.content_box {
  width: 30em;
  margin: auto;
}

pre {
  padding: 0.7rem 1rem !important;
  border-radius: 5px !important;
  background: #000000 !important;
  color: #ffffff !important;
  font-size: 0.8rem !important;
  overflow-x: auto !important;

  code {
    display: block !important;
    background: #dddddd !important;
    color: #000000 !important;
  }
}

p code {
  padding: 0.2rem 0.4rem !important;
  border-radius: 5px !important;
  font-size: 0.8rem !important ;
  font-weight: bold !important;
  background: rgba(#000000, 0.1) !important;
  color: rgba(#000000, 0.8) !important;
}
</style>