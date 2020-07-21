<template>
  <div class="editor">
    <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
      <div class="menubar">

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bold() }"
          @click="commands.bold"
        >
        <!-- bold -->
        <v-icon>mdi-format-bold</v-icon>
          <!-- <icon name="bold" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.italic() }"
          @click="commands.italic"
        >
        <v-icon>mdi-format-italic</v-icon>
        <!-- italic -->
          <!-- <icon name="italic" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.strike() }"
          @click="commands.strike"
        >
        <v-icon>mdi-format-strikethrough-variant</v-icon>
                <!-- strike
          <icon name="strike" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.underline() }"
          @click="commands.underline"
        >
        <v-icon>mdi-format-underline</v-icon>
        <!-- underline
          <icon name="underline" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code() }"
          @click="commands.code"
        >
        <v-icon>mdi-code-tags</v-icon>
        <!-- code -->
          <!-- <icon name="code" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.paragraph() }"
          @click="commands.paragraph"
        >
        <!-- paragraph -->
        <v-icon>mdi-format-paragraph</v-icon>
          <!-- <icon name="paragraph" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 1 }) }"
          @click="commands.heading({ level: 1 })"
        >
          H1
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 2 }) }"
          @click="commands.heading({ level: 2 })"
        >
          H2
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 3 }) }"
          @click="commands.heading({ level: 3 })"
        >
          H3
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bullet_list() }"
          @click="commands.bullet_list"
        >
        <v-icon>mdi-format-list-bulleted</v-icon>
        <!--  bullet_list
          <icon name="ul" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.ordered_list() }"
          @click="commands.ordered_list"
        >
        <v-icon>mdi-format-list-numbered</v-icon>
        
        <!-- ordered_list
          <icon name="ol" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.blockquote() }"
          @click="commands.blockquote"
        >
        <v-icon>mdi-format-quote-close</v-icon>
        
        <!-- blockquote
          <icon name="quote" /> -->
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code_block() }"
          @click="commands.code_block"
        >code_block
          <icon name="code" />
        </button>

        <button
          class="menubar__button"
          @click="commands.horizontal_rule"
        >
        <v-icon>mdi-drag-horizontal-variant</v-icon>
        
        <!-- horizontal_rule
          <icon name="hr" /> -->
        </button>

        <button
          class="menubar__button"
          @click="commands.undo"
        >
        <v-icon>mdi-undo</v-icon>
        <!-- undo
          <icon name="undo" /> -->
        </button>

        <button
          class="menubar__button"
          @click="commands.redo"
        >
        <v-icon>mdi-redo</v-icon>
        <!-- redo
          <icon name="redo" /> -->
        </button>

      </div>
    </editor-menu-bar>
 

    <editor-content class="editor__content" :editor="editor" />
  </div>
</template>

<script>
import '../assets/sass/main.scss'
// import Icon from 'Components/Icon'
import { Editor, EditorContent, EditorMenuBar } from 'tiptap'
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
} from 'tiptap-extensions'
export default {
  components: {
    EditorContent,
    EditorMenuBar,
    // Icon,
  },
  data() {
    return {
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
      }),
    }
  },
  beforeDestroy() {
    this.editor.destroy()
  },
}
</script>