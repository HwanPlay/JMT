module.exports = {
  'transpileDependencies': [
    'vuetify'
  ],
  pluginOptions: {
    electronBuilder: {
      builderOptions: {
        nsis: {
          oneClick: false,
          perMachine: true,
          allowToChangeInstallationDirectory: true,
          language: 1042,
          shortcutName: "Join Meeting & Typing"
        },
        win: {
          target: [
            {
              target: "nsis",
              arch: [
                'x64'
              ]
            }
          ],
          icon: "./assets/icons/JMTLogo.png"
        },
        mac: {
          target: [
            'default'
          ],
          icon: './assets/icons/JMTLogo.png'
        },
        dmg: {
          title: "Join Meeting & Typing",
          icon: './assets/icons/JMTLogo.png'
        },
      }
    }
  }
}