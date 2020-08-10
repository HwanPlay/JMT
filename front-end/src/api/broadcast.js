'use strict';

// Last time updated: 2019-06-15 4:26:11 PM UTC

// _________________________
// RTCMultiConnection v3.6.9

// Open-Sourced: https://github.com/muaz-khan/RTCMultiConnection

// --------------------------------------------------
// Muaz Khan     - www.MuazKhan.com
// MIT License   - www.WebRTC-Experiment.com/licence
// --------------------------------------------------

var RTCMultiConnection = function(roomid, forceOptions) {

  var browserFakeUserAgent = 'Fake/5.0 (FakeOS) AppleWebKit/123 (KHTML, like Gecko) Fake/12.3.4567.89 Fake/123.45';

  (function(that) {
    if (!that) {
      return;
    }

    if (typeof window !== 'undefined') {
      return;
    }

    if (typeof global === 'undefined') {
      return;
    }

    global.navigator = {
      userAgent: browserFakeUserAgent,
      getUserMedia: function() {}
    };

    if (!global.console) {
      global.console = {};
    }

    if (typeof global.console.debug === 'undefined') {
      global.console.debug = global.console.info = global.console.error = global.console.log = global.console.log || function() {
        console.log(arguments);
      };
    }

    if (typeof document === 'undefined') {
      /*global document:true */
      that.document = {};

      document.createElement = document.captureStream = document.mozCaptureStream = function() {
        var obj = {
          getContext: function() {
            return obj;
          },
          play: function() {},
          pause: function() {},
          drawImage: function() {},
          toDataURL: function() {
            return '';
          }
        };
        return obj;
      };

      document.addEventListener = document.removeEventListener = that.addEventListener = that.removeEventListener = function() {};

      that.HTMLVideoElement = that.HTMLMediaElement = function() {};
    }

    if (typeof io === 'undefined') {
      that.io = function() {
        return {
          on: function(eventName, callback) {
            callback = callback || function() {};

            if (eventName === 'connect') {
              callback();
            }
          },
          emit: function(eventName, data, callback) {
            callback = callback || function() {};
            if (eventName === 'open-room' || eventName === 'join-room') {
              callback(true, data.sessionid, null);
            }
          }
        };
      };
    }

    if (typeof location === 'undefined') {
      /*global location:true */
      that.location = {
        protocol: 'file:',
        href: '',
        hash: '',
        origin: 'self'
      };
    }

    if (typeof screen === 'undefined') {
      /*global screen:true */
      that.screen = {
        width: 0,
        height: 0
      };
    }

    if (typeof URL === 'undefined') {
      /*global screen:true */
      that.URL = {
        createObjectURL: function() {
          return '';
        },
        revokeObjectURL: function() {
          return '';
        }
      };
    }

    /*global window:true */
    that.window = global;
  })(typeof global !== 'undefined' ? global : null);

  function SocketConnection(broadcast, connectCallback) {
    function isData(session) {
      return !session.audio && !session.video && !session.screen && session.data;
    }

    var parameters = '';

    parameters += '?userid=' + broadcast.userid;
    parameters += '&sessionid=' + broadcast.sessionid;
    parameters += '&msgEvent=' + broadcast.socketMessageEvent;
    parameters += '&socketCustomEvent=' + broadcast.socketCustomEvent;
    parameters += '&autoCloseEntireSession=' + !!broadcast.autoCloseEntireSession;

    if (broadcast.session.broadcast === true) {
      parameters += '&oneToMany=true';
    }

    parameters += '&maxParticipantsAllowed=' + broadcast.maxParticipantsAllowed;

    if (broadcast.enableScalableBroadcast) {
      parameters += '&enableScalableBroadcast=true';
      parameters += '&maxRelayLimitPerUser=' + (broadcast.maxRelayLimitPerUser || 2);
    }

    parameters += '&extra=' + JSON.stringify(broadcast.extra || {});

    if (broadcast.socketCustomParameters) {
      parameters += broadcast.socketCustomParameters;
    }

    try {
      io.sockets = {};
    } catch (e) {}

    if (!broadcast.socketURL) {
      broadcast.socketURL = '/';
    }

    if (broadcast.socketURL.substr(broadcast.socketURL.length - 1, 1) != '/') {
      // broadcast.socketURL = 'https://domain.com:9001/';
      throw '"socketURL" MUST end with a slash.';
    }

    if (broadcast.enableLogs) {
      if (broadcast.socketURL == '/') {
        console.info('socket.io url is: ', location.origin + '/');
      } else {
        console.info('socket.io url is: ', broadcast.socketURL);
      }
    }

    try {
      broadcast.socket = io(broadcast.socketURL + parameters);
    } catch (e) {
      broadcast.socket = io.connect(broadcast.socketURL + parameters, broadcast.socketOptions);
    }

    var mPeer = broadcast.multiPeersHandler;

    broadcast.socket.on('extra-data-updated', function(remoteUserId, extra) {
      if (!broadcast.peers[remoteUserId]) return;
      broadcast.peers[remoteUserId].extra = extra;

      broadcast.onExtraDataUpdated({
        userid: remoteUserId,
        extra: extra
      });

      updateExtraBackup(remoteUserId, extra);
    });

    function updateExtraBackup(remoteUserId, extra) {
      if (!broadcast.peersBackup[remoteUserId]) {
        broadcast.peersBackup[remoteUserId] = {
          userid: remoteUserId,
          extra: {}
        };
      }

      broadcast.peersBackup[remoteUserId].extra = extra;
    }

    function onMessageEvent(message) {
      if (message.remoteUserId != broadcast.userid) return;

      if (broadcast.peers[message.sender] && broadcast.peers[message.sender].extra != message.message.extra) {
        broadcast.peers[message.sender].extra = message.extra;
        broadcast.onExtraDataUpdated({
          userid: message.sender,
          extra: message.extra
        });

        updateExtraBackup(message.sender, message.extra);
      }

      if (message.message.streamSyncNeeded && broadcast.peers[message.sender]) {
        var stream = broadcast.streamEvents[message.message.streamid];
        if (!stream || !stream.stream) {
          return;
        }

        var action = message.message.action;

        if (action === 'ended' || action === 'inactive' || action === 'stream-removed') {
          if (broadcast.peersBackup[stream.userid]) {
            stream.extra = broadcast.peersBackup[stream.userid].extra;
          }
          broadcast.onstreamended(stream);
          return;
        }

        var type = message.message.type != 'both' ? message.message.type : null;

        if (typeof stream.stream[action] == 'function') {
          stream.stream[action](type);
        }
        return;
      }

      if (message.message === 'dropPeerConnection') {
        broadcast.deletePeer(message.sender);
        return;
      }

      if (message.message.allParticipants) {
        if (message.message.allParticipants.indexOf(message.sender) === -1) {
          message.message.allParticipants.push(message.sender);
        }

        message.message.allParticipants.forEach(function(participant) {
          mPeer[!broadcast.peers[participant] ? 'createNewPeer' : 'renegotiatePeer'](participant, {
            localPeerSdpConstraints: {
              OfferToReceiveAudio: broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
              OfferToReceiveVideo: broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
            },
            remotePeerSdpConstraints: {
              OfferToReceiveAudio: broadcast.session.oneway ? !!broadcast.session.audio : broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
              OfferToReceiveVideo: broadcast.session.oneway ? !!broadcast.session.video || !!broadcast.session.screen : broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
            },
            isOneWay: !!broadcast.session.oneway || broadcast.direction === 'one-way',
            isDataOnly: isData(broadcast.session)
          });
        });
        return;
      }

      if (message.message.newParticipant) {
        if (message.message.newParticipant == broadcast.userid) return;
        if (broadcast.peers[message.message.newParticipant]) return;

        mPeer.createNewPeer(message.message.newParticipant, message.message.userPreferences || {
          localPeerSdpConstraints: {
            OfferToReceiveAudio: broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
            OfferToReceiveVideo: broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
          },
          remotePeerSdpConstraints: {
            OfferToReceiveAudio: broadcast.session.oneway ? !!broadcast.session.audio : broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
            OfferToReceiveVideo: broadcast.session.oneway ? !!broadcast.session.video || !!broadcast.session.screen : broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
          },
          isOneWay: !!broadcast.session.oneway || broadcast.direction === 'one-way',
          isDataOnly: isData(broadcast.session)
        });
        return;
      }

      if (message.message.readyForOffer) {
        if (broadcast.attachStreams.length) {
          broadcast.waitingForLocalMedia = false;
        }

        if (broadcast.waitingForLocalMedia) {
          // if someone is waiting to join you
          // make sure that we've local media before making a handshake
          setTimeout(function() {
            onMessageEvent(message);
          }, 1);
          return;
        }
      }

      if (message.message.newParticipationRequest && message.sender !== broadcast.userid) {
        if (broadcast.peers[message.sender]) {
          broadcast.deletePeer(message.sender);
        }

        var userPreferences = {
          extra: message.extra || {},
          localPeerSdpConstraints: message.message.remotePeerSdpConstraints || {
            OfferToReceiveAudio: broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
            OfferToReceiveVideo: broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
          },
          remotePeerSdpConstraints: message.message.localPeerSdpConstraints || {
            OfferToReceiveAudio: broadcast.session.oneway ? !!broadcast.session.audio : broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
            OfferToReceiveVideo: broadcast.session.oneway ? !!broadcast.session.video || !!broadcast.session.screen : broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
          },
          isOneWay: typeof message.message.isOneWay !== 'undefined' ? message.message.isOneWay : !!broadcast.session.oneway || broadcast.direction === 'one-way',
          isDataOnly: typeof message.message.isDataOnly !== 'undefined' ? message.message.isDataOnly : isData(broadcast.session),
          dontGetRemoteStream: typeof message.message.isOneWay !== 'undefined' ? message.message.isOneWay : !!broadcast.session.oneway || broadcast.direction === 'one-way',
          dontAttachLocalStream: !!message.message.dontGetRemoteStream,
          broadcastDescription: message,
          successCallback: function() {}
        };

        broadcast.onNewParticipant(message.sender, userPreferences);
        return;
      }

      if (message.message.changedUUID) {
        if (broadcast.peers[message.message.oldUUID]) {
          broadcast.peers[message.message.newUUID] = broadcast.peers[message.message.oldUUID];
          delete broadcast.peers[message.message.oldUUID];
        }
      }

      if (message.message.userLeft) {
        mPeer.onUserLeft(message.sender);

        if (message.message.autoCloseEntireSession) {
          broadcast.leave();
        }

        return;
      }

      mPeer.addNegotiatedMessage(message.message, message.sender);
    }

    broadcast.socket.on(broadcast.socketMessageEvent, onMessageEvent);

    var alreadyConnected = false;

    broadcast.socket.resetProps = function() {
      alreadyConnected = false;
    };

    broadcast.socket.on('connect', function() {
      if (alreadyConnected) {
        return;
      }
      alreadyConnected = true;

      if (broadcast.enableLogs) {
        console.info('socket.io broadcast is opened.');
      }

      setTimeout(function() {
        broadcast.socket.emit('extra-data-updated', broadcast.extra);
      }, 1000);

      if (connectCallback) {
        connectCallback(broadcast.socket);
      }
    });

    broadcast.socket.on('disconnect', function(event) {
      broadcast.onSocketDisconnect(event);
    });

    broadcast.socket.on('error', function(event) {
      broadcast.onSocketError(event);
    });

    broadcast.socket.on('user-disconnected', function(remoteUserId) {
      if (remoteUserId === broadcast.userid) {
        return;
      }

      broadcast.onUserStatusChanged({
        userid: remoteUserId,
        status: 'offline',
        extra: broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra || {} : {}
      });

      broadcast.deletePeer(remoteUserId);
    });

    broadcast.socket.on('user-connected', function(userid) {
      if (userid === broadcast.userid) {
        return;
      }

      broadcast.onUserStatusChanged({
        userid: userid,
        status: 'online',
        extra: broadcast.peers[userid] ? broadcast.peers[userid].extra || {} : {}
      });
    });

    broadcast.socket.on('closed-entire-session', function(sessionid, extra) {
      broadcast.leave();
      broadcast.onEntireSessionClosed({
        sessionid: sessionid,
        userid: sessionid,
        extra: extra
      });
    });

    broadcast.socket.on('userid-already-taken', function(useridAlreadyTaken, yourNewUserId) {
      broadcast.onUserIdAlreadyTaken(useridAlreadyTaken, yourNewUserId);
    });

    broadcast.socket.on('logs', function(log) {
      if (!broadcast.enableLogs) return;
      console.debug('server-logs', log);
    });

    broadcast.socket.on('number-of-broadcast-viewers-updated', function(data) {
      broadcast.onNumberOfBroadcastViewersUpdated(data);
    });

    broadcast.socket.on('set-isInitiator-true', function(sessionid) {
      if (sessionid != broadcast.sessionid) return;
      broadcast.isInitiator = true;
    });
  }

  function MultiPeers(broadcast) {
    var self = this;

    var skipPeers = ['getAllParticipants', 'getLength', 'selectFirst', 'streams', 'send', 'forEach'];
    broadcast.peers = {
      getLength: function() {
        var numberOfPeers = 0;
        for (var peer in this) {
          if (skipPeers.indexOf(peer) == -1) {
            numberOfPeers++;
          }
        }
        return numberOfPeers;
      },
      selectFirst: function() {
        var firstPeer;
        for (var peer in this) {
          if (skipPeers.indexOf(peer) == -1) {
            firstPeer = this[peer];
          }
        }
        return firstPeer;
      },
      getAllParticipants: function(sender) {
        var allPeers = [];
        for (var peer in this) {
          if (skipPeers.indexOf(peer) == -1 && peer != sender) {
            allPeers.push(peer);
          }
        }
        return allPeers;
      },
      forEach: function(callbcak) {
        this.getAllParticipants().forEach(function(participant) {
          callbcak(broadcast.peers[participant]);
        });
      },
      send: function(data, remoteUserId) {
        var that = this;

        if (!isNull(data.size) && !isNull(data.type)) {
          if (broadcast.enableFileSharing) {
            self.shareFile(data, remoteUserId);
            return;
          }

          if (typeof data !== 'string') {
            data = JSON.stringify(data);
          }
        }

        if (data.type !== 'text' && !(data instanceof ArrayBuffer) && !(data instanceof DataView)) {
          TextSender.send({
            text: data,
            channel: this,
            broadcast: broadcast,
            remoteUserId: remoteUserId
          });
          return;
        }

        if (data.type === 'text') {
          data = JSON.stringify(data);
        }

        if (remoteUserId) {
          var remoteUser = broadcast.peers[remoteUserId];
          if (remoteUser) {
            if (!remoteUser.channels.length) {
              broadcast.peers[remoteUserId].createDataChannel();
              broadcast.renegotiate(remoteUserId);
              setTimeout(function() {
                that.send(data, remoteUserId);
              }, 3000);
              return;
            }

            remoteUser.channels.forEach(function(channel) {
              channel.send(data);
            });
            return;
          }
        }

        this.getAllParticipants().forEach(function(participant) {
          if (!that[participant].channels.length) {
            broadcast.peers[participant].createDataChannel();
            broadcast.renegotiate(participant);
            setTimeout(function() {
              that[participant].channels.forEach(function(channel) {
                channel.send(data);
              });
            }, 3000);
            return;
          }

          that[participant].channels.forEach(function(channel) {
            channel.send(data);
          });
        });
      }
    };

    this.uuid = broadcast.userid;

    this.getLocalConfig = function(remoteSdp, remoteUserId, userPreferences) {
      if (!userPreferences) {
        userPreferences = {};
      }

      return {
        streamsToShare: userPreferences.streamsToShare || {},
        rtcMultiConnection: broadcast,
        broadcastDescription: userPreferences.broadcastDescription,
        userid: remoteUserId,
        localPeerSdpConstraints: userPreferences.localPeerSdpConstraints,
        remotePeerSdpConstraints: userPreferences.remotePeerSdpConstraints,
        dontGetRemoteStream: !!userPreferences.dontGetRemoteStream,
        dontAttachLocalStream: !!userPreferences.dontAttachLocalStream,
        renegotiatingPeer: !!userPreferences.renegotiatingPeer,
        peerRef: userPreferences.peerRef,
        channels: userPreferences.channels || [],
        onLocalSdp: function(localSdp) {
          self.onNegotiationNeeded(localSdp, remoteUserId);
        },
        onLocalCandidate: function(localCandidate) {
          localCandidate = OnIceCandidateHandler.processCandidates(broadcast, localCandidate);
          if (localCandidate) {
            self.onNegotiationNeeded(localCandidate, remoteUserId);
          }
        },
        remoteSdp: remoteSdp,
        onDataChannelMessage: function(message) {
          if (!broadcast.fbr && broadcast.enableFileSharing) initFileBufferReader();

          if (typeof message == 'string' || !broadcast.enableFileSharing) {
            self.onDataChannelMessage(message, remoteUserId);
            return;
          }

          var that = this;

          if (message instanceof ArrayBuffer || message instanceof DataView) {
            broadcast.fbr.convertToObject(message, function(object) {
              that.onDataChannelMessage(object);
            });
            return;
          }

          if (message.readyForNextChunk) {
            broadcast.fbr.getNextChunk(message, function(nextChunk, isLastChunk) {
              broadcast.peers[remoteUserId].channels.forEach(function(channel) {
                channel.send(nextChunk);
              });
            }, remoteUserId);
            return;
          }

          if (message.chunkMissing) {
            broadcast.fbr.chunkMissing(message);
            return;
          }

          broadcast.fbr.addChunk(message, function(promptNextChunk) {
            broadcast.peers[remoteUserId].peer.channel.send(promptNextChunk);
          });
        },
        onDataChannelError: function(error) {
          self.onDataChannelError(error, remoteUserId);
        },
        onDataChannelOpened: function(channel) {
          self.onDataChannelOpened(channel, remoteUserId);
        },
        onDataChannelClosed: function(event) {
          self.onDataChannelClosed(event, remoteUserId);
        },
        onRemoteStream: function(stream) {
          if (broadcast.peers[remoteUserId]) {
            broadcast.peers[remoteUserId].streams.push(stream);
          }

          self.onGettingRemoteMedia(stream, remoteUserId);
        },
        onRemoteStreamRemoved: function(stream) {
          self.onRemovingRemoteMedia(stream, remoteUserId);
        },
        onPeerStateChanged: function(states) {
          self.onPeerStateChanged(states);

          if (states.iceConnectionState === 'new') {
            self.onNegotiationStarted(remoteUserId, states);
          }

          if (states.iceConnectionState === 'connected') {
            self.onNegotiationCompleted(remoteUserId, states);
          }

          if (states.iceConnectionState.search(/closed|failed/gi) !== -1) {
            self.onUserLeft(remoteUserId);
            self.disconnectWith(remoteUserId);
          }
        }
      };
    };

    this.createNewPeer = function(remoteUserId, userPreferences) {
      if (broadcast.maxParticipantsAllowed <= broadcast.getAllParticipants().length) {
        return;
      }

      userPreferences = userPreferences || {};

      if (broadcast.isInitiator && !!broadcast.session.audio && broadcast.session.audio === 'two-way' && !userPreferences.streamsToShare) {
        userPreferences.isOneWay = false;
        userPreferences.isDataOnly = false;
        userPreferences.session = broadcast.session;
      }

      if (!userPreferences.isOneWay && !userPreferences.isDataOnly) {
        userPreferences.isOneWay = true;
        this.onNegotiationNeeded({
          enableMedia: true,
          userPreferences: userPreferences
        }, remoteUserId);
        return;
      }

      userPreferences = broadcast.setUserPreferences(userPreferences, remoteUserId);
      var localConfig = this.getLocalConfig(null, remoteUserId, userPreferences);
      broadcast.peers[remoteUserId] = new PeerInitiator(localConfig);
    };

    this.createAnsweringPeer = function(remoteSdp, remoteUserId, userPreferences) {
      userPreferences = broadcast.setUserPreferences(userPreferences || {}, remoteUserId);

      var localConfig = this.getLocalConfig(remoteSdp, remoteUserId, userPreferences);
      broadcast.peers[remoteUserId] = new PeerInitiator(localConfig);
    };

    this.renegotiatePeer = function(remoteUserId, userPreferences, remoteSdp) {
      if (!broadcast.peers[remoteUserId]) {
        if (broadcast.enableLogs) {
          console.error('Peer (' + remoteUserId + ') does not exist. Renegotiation skipped.');
        }
        return;
      }

      if (!userPreferences) {
        userPreferences = {};
      }

      userPreferences.renegotiatingPeer = true;
      userPreferences.peerRef = broadcast.peers[remoteUserId].peer;
      userPreferences.channels = broadcast.peers[remoteUserId].channels;

      var localConfig = this.getLocalConfig(remoteSdp, remoteUserId, userPreferences);

      broadcast.peers[remoteUserId] = new PeerInitiator(localConfig);
    };

    this.replaceTrack = function(track, remoteUserId, isVideoTrack) {
      if (!broadcast.peers[remoteUserId]) {
        throw 'This peer (' + remoteUserId + ') does not exist.';
      }

      var peer = broadcast.peers[remoteUserId].peer;

      if (!!peer.getSenders && typeof peer.getSenders === 'function' && peer.getSenders().length) {
        peer.getSenders().forEach(function(rtpSender) {
          if (isVideoTrack && rtpSender.track.kind === 'video') {
            broadcast.peers[remoteUserId].peer.lastVideoTrack = rtpSender.track;
            rtpSender.replaceTrack(track);
          }

          if (!isVideoTrack && rtpSender.track.kind === 'audio') {
            broadcast.peers[remoteUserId].peer.lastAudioTrack = rtpSender.track;
            rtpSender.replaceTrack(track);
          }
        });
        return;
      }

      console.warn('RTPSender.replaceTrack is NOT supported.');
      this.renegotiatePeer(remoteUserId);
    };

    this.onNegotiationNeeded = function(message, remoteUserId) {};
    this.addNegotiatedMessage = function(message, remoteUserId) {
      if (message.type && message.sdp) {
        if (message.type == 'answer') {
          if (broadcast.peers[remoteUserId]) {
            broadcast.peers[remoteUserId].addRemoteSdp(message);
          }
        }

        if (message.type == 'offer') {
          if (message.renegotiatingPeer) {
            this.renegotiatePeer(remoteUserId, null, message);
          } else {
            this.createAnsweringPeer(message, remoteUserId);
          }
        }

        if (broadcast.enableLogs) {
          console.log('Remote peer\'s sdp:', message.sdp);
        }
        return;
      }

      if (message.candidate) {
        if (broadcast.peers[remoteUserId]) {
          broadcast.peers[remoteUserId].addRemoteCandidate(message);
        }

        if (broadcast.enableLogs) {
          console.log('Remote peer\'s candidate pairs:', message.candidate);
        }
        return;
      }

      if (message.enableMedia) {
        broadcast.session = message.userPreferences.session || broadcast.session;

        if (broadcast.session.oneway && broadcast.attachStreams.length) {
          broadcast.attachStreams = [];
        }

        if (message.userPreferences.isDataOnly && broadcast.attachStreams.length) {
          broadcast.attachStreams.length = [];
        }

        var streamsToShare = {};
        broadcast.attachStreams.forEach(function(stream) {
          streamsToShare[stream.streamid] = {
            isAudio: !!stream.isAudio,
            isVideo: !!stream.isVideo,
            isScreen: !!stream.isScreen
          };
        });
        message.userPreferences.streamsToShare = streamsToShare;

        self.onNegotiationNeeded({
          readyForOffer: true,
          userPreferences: message.userPreferences
        }, remoteUserId);
      }

      if (message.readyForOffer) {
        broadcast.onReadyForOffer(remoteUserId, message.userPreferences);
      }

      function cb(stream) {
        gumCallback(stream, message, remoteUserId);
      }
    };

    function gumCallback(stream, message, remoteUserId) {
      var streamsToShare = {};
      broadcast.attachStreams.forEach(function(stream) {
        streamsToShare[stream.streamid] = {
          isAudio: !!stream.isAudio,
          isVideo: !!stream.isVideo,
          isScreen: !!stream.isScreen
        };
      });
      message.userPreferences.streamsToShare = streamsToShare;

      self.onNegotiationNeeded({
        readyForOffer: true,
        userPreferences: message.userPreferences
      }, remoteUserId);
    }

    this.onGettingRemoteMedia = function(stream, remoteUserId) {};
    this.onRemovingRemoteMedia = function(stream, remoteUserId) {};
    this.onGettingLocalMedia = function(localStream) {};
    this.onLocalMediaError = function(error, constraints) {
      broadcast.onMediaError(error, constraints);
    };

    function initFileBufferReader() {
      broadcast.fbr = new FileBufferReader();
      broadcast.fbr.onProgress = function(chunk) {
        broadcast.onFileProgress(chunk);
      };
      broadcast.fbr.onBegin = function(file) {
        broadcast.onFileStart(file);
      };
      broadcast.fbr.onEnd = function(file) {
        broadcast.onFileEnd(file);
      };
    }

    this.shareFile = function(file, remoteUserId) {
      initFileBufferReader();

      broadcast.fbr.readAsArrayBuffer(file, function(uuid) {
        var arrayOfUsers = broadcast.getAllParticipants();

        if (remoteUserId) {
          arrayOfUsers = [remoteUserId];
        }

        arrayOfUsers.forEach(function(participant) {
          broadcast.fbr.getNextChunk(uuid, function(nextChunk) {
            broadcast.peers[participant].channels.forEach(function(channel) {
              channel.send(nextChunk);
            });
          }, participant);
        });
      }, {
        userid: broadcast.userid,
        // extra: broadcast.extra,
        chunkSize: DetectRTC.browser.name === 'Firefox' ? 15 * 1000 : broadcast.chunkSize || 0
      });
    };

    if (typeof 'TextReceiver' !== 'undefined') {
      var textReceiver = new TextReceiver(broadcast);
    }

    this.onDataChannelMessage = function(message, remoteUserId) {
      textReceiver.receive(JSON.parse(message), remoteUserId, broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra : {});
    };

    this.onDataChannelClosed = function(event, remoteUserId) {
      event.userid = remoteUserId;
      event.extra = broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra : {};
      broadcast.onclose(event);
    };

    this.onDataChannelError = function(error, remoteUserId) {
      error.userid = remoteUserId;
      event.extra = broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra : {};
      broadcast.onerror(error);
    };

    this.onDataChannelOpened = function(channel, remoteUserId) {
      // keep last channel only; we are not expecting parallel/channels channels
      if (broadcast.peers[remoteUserId].channels.length) {
        broadcast.peers[remoteUserId].channels = [channel];
        return;
      }

      broadcast.peers[remoteUserId].channels.push(channel);
      broadcast.onopen({
        userid: remoteUserId,
        extra: broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra : {},
        channel: channel
      });
    };

    this.onPeerStateChanged = function(state) {
      broadcast.onPeerStateChanged(state);
    };

    this.onNegotiationStarted = function(remoteUserId, states) {};
    this.onNegotiationCompleted = function(remoteUserId, states) {};

    this.getRemoteStreams = function(remoteUserId) {
      remoteUserId = remoteUserId || broadcast.peers.getAllParticipants()[0];
      return broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].streams : [];
    };
  }

  'use strict';

  // Last Updated On: 2019-01-10 5:32:55 AM UTC

  // ________________
  // DetectRTC v1.3.9

  // Open-Sourced: https://github.com/muaz-khan/DetectRTC

  // --------------------------------------------------
  // Muaz Khan     - www.MuazKhan.com
  // MIT License   - www.WebRTC-Experiment.com/licence
  // --------------------------------------------------

  (function() {

    var browserFakeUserAgent = 'Fake/5.0 (FakeOS) AppleWebKit/123 (KHTML, like Gecko) Fake/12.3.4567.89 Fake/123.45';

    var isNodejs = typeof process === 'object' && typeof process.versions === 'object' && process.versions.node && /*node-process*/ !process.browser;
    if (isNodejs) {
      var version = process.versions.node.toString().replace('v', '');
      browserFakeUserAgent = 'Nodejs/' + version + ' (NodeOS) AppleWebKit/' + version + ' (KHTML, like Gecko) Nodejs/' + version + ' Nodejs/' + version;
    }

    (function(that) {
      if (typeof window !== 'undefined') {
        return;
      }

      if (typeof window === 'undefined' && typeof global !== 'undefined') {
        global.navigator = {
          userAgent: browserFakeUserAgent,
          getUserMedia: function() {}
        };

        /*global window:true */
        that.window = global;
      } else if (typeof window === 'undefined') {
        // window = this;
      }

      if (typeof location === 'undefined') {
        /*global location:true */
        that.location = {
          protocol: 'file:',
          href: '',
          hash: ''
        };
      }

      if (typeof screen === 'undefined') {
        /*global screen:true */
        that.screen = {
          width: 0,
          height: 0
        };
      }
    })(typeof global !== 'undefined' ? global : window);

    /*global navigator:true */
    var navigator = window.navigator;

    if (typeof navigator !== 'undefined') {
      if (typeof navigator.webkitGetUserMedia !== 'undefined') {
        navigator.getUserMedia = navigator.webkitGetUserMedia;
      }

      if (typeof navigator.mozGetUserMedia !== 'undefined') {
        navigator.getUserMedia = navigator.mozGetUserMedia;
      }
    } else {
      navigator = {
        getUserMedia: function() {},
        userAgent: browserFakeUserAgent
      };
    }

    var isMobileDevice = !!(/Android|webOS|iPhone|iPad|iPod|BB10|BlackBerry|IEMobile|Opera Mini|Mobile|mobile/i.test(navigator.userAgent || ''));

    var isEdge = navigator.userAgent.indexOf('Edge') !== -1 && (!!navigator.msSaveOrOpenBlob || !!navigator.msSaveBlob);

    var isOpera = !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
    var isFirefox = typeof window.InstallTrigger !== 'undefined';
    var isSafari = /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
    var isChrome = !!window.chrome && !isOpera;
    var isIE = typeof document !== 'undefined' && !!document.documentMode && !isEdge;

    // this one can also be used:
    // https://www.websocket.org/js/stuff.js (DetectBrowser.js)

    function getBrowserInfo() {
      var nVer = navigator.appVersion;
      var nAgt = navigator.userAgent;
      var browserName = navigator.appName;
      var fullVersion = '' + parseFloat(navigator.appVersion);
      var majorVersion = parseInt(navigator.appVersion, 10);
      var nameOffset, verOffset, ix;

      // both and safri and chrome has same userAgent
      if (isSafari && !isChrome && nAgt.indexOf('CriOS') !== -1) {
        isSafari = false;
        isChrome = true;
      }

      // In Opera, the true version is after 'Opera' or after 'Version'
      if (isOpera) {
        browserName = 'Opera';
        try {
          fullVersion = navigator.userAgent.split('OPR/')[1].split(' ')[0];
          majorVersion = fullVersion.split('.')[0];
        } catch (e) {
          fullVersion = '0.0.0.0';
          majorVersion = 0;
        }
      }
      // In MSIE version <=10, the true version is after 'MSIE' in userAgent
      // In IE 11, look for the string after 'rv:'
      else if (isIE) {
        verOffset = nAgt.indexOf('rv:');
        if (verOffset > 0) { //IE 11
          fullVersion = nAgt.substring(verOffset + 3);
        } else { //IE 10 or earlier
          verOffset = nAgt.indexOf('MSIE');
          fullVersion = nAgt.substring(verOffset + 5);
        }
        browserName = 'IE';
      }
      // In Chrome, the true version is after 'Chrome' 
      else if (isChrome) {
        verOffset = nAgt.indexOf('Chrome');
        browserName = 'Chrome';
        fullVersion = nAgt.substring(verOffset + 7);
      }
      // In Safari, the true version is after 'Safari' or after 'Version' 
      else if (isSafari) {
        verOffset = nAgt.indexOf('Safari');

        browserName = 'Safari';
        fullVersion = nAgt.substring(verOffset + 7);

        if ((verOffset = nAgt.indexOf('Version')) !== -1) {
          fullVersion = nAgt.substring(verOffset + 8);
        }

        if (navigator.userAgent.indexOf('Version/') !== -1) {
          fullVersion = navigator.userAgent.split('Version/')[1].split(' ')[0];
        }
      }
      // In Firefox, the true version is after 'Firefox' 
      else if (isFirefox) {
        verOffset = nAgt.indexOf('Firefox');
        browserName = 'Firefox';
        fullVersion = nAgt.substring(verOffset + 8);
      }

      // In most other browsers, 'name/version' is at the end of userAgent 
      else if ((nameOffset = nAgt.lastIndexOf(' ') + 1) < (verOffset = nAgt.lastIndexOf('/'))) {
        browserName = nAgt.substring(nameOffset, verOffset);
        fullVersion = nAgt.substring(verOffset + 1);

        if (browserName.toLowerCase() === browserName.toUpperCase()) {
          browserName = navigator.appName;
        }
      }

      if (isEdge) {
        browserName = 'Edge';
        fullVersion = navigator.userAgent.split('Edge/')[1];
        // fullVersion = parseInt(navigator.userAgent.match(/Edge\/(\d+).(\d+)$/)[2], 10).toString();
      }

      // trim the fullVersion string at semicolon/space/bracket if present
      if ((ix = fullVersion.search(/[; \)]/)) !== -1) {
        fullVersion = fullVersion.substring(0, ix);
      }

      majorVersion = parseInt('' + fullVersion, 10);

      if (isNaN(majorVersion)) {
        fullVersion = '' + parseFloat(navigator.appVersion);
        majorVersion = parseInt(navigator.appVersion, 10);
      }

      return {
        fullVersion: fullVersion,
        version: majorVersion,
        name: browserName,
        isPrivateBrowsing: false
      };
    }

    // via: https://gist.github.com/cou929/7973956

    function retry(isDone, next) {
      var currentTrial = 0,
        maxRetry = 50,
        interval = 10,
        isTimeout = false;
      var id = window.setInterval(
        function() {
          if (isDone()) {
            window.clearInterval(id);
            next(isTimeout);
          }
          if (currentTrial++ > maxRetry) {
            window.clearInterval(id);
            isTimeout = true;
            next(isTimeout);
          }
        },
        10
      );
    }

    function isIE10OrLater(userAgent) {
      var ua = userAgent.toLowerCase();
      if (ua.indexOf('msie') === 0 && ua.indexOf('trident') === 0) {
        return false;
      }
      var match = /(?:msie|rv:)\s?([\d\.]+)/.exec(ua);
      if (match && parseInt(match[1], 10) >= 10) {
        return true;
      }
      return false;
    }

    function detectPrivateMode(callback) {
      var isPrivate;

      try {

        if (window.webkitRequestFileSystem) {
          window.webkitRequestFileSystem(
            window.TEMPORARY, 1,
            function() {
              isPrivate = false;
            },
            function(e) {
              isPrivate = true;
            }
          );
        } else if (window.indexedDB && /Firefox/.test(window.navigator.userAgent)) {
          var db;
          try {
            db = window.indexedDB.open('test');
            db.onerror = function() {
              return true;
            };
          } catch (e) {
            isPrivate = true;
          }

          if (typeof isPrivate === 'undefined') {
            retry(
              function isDone() {
                return db.readyState === 'done' ? true : false;
              },
              function next(isTimeout) {
                if (!isTimeout) {
                  isPrivate = db.result ? false : true;
                }
              }
            );
          }
        } else if (isIE10OrLater(window.navigator.userAgent)) {
          isPrivate = false;
          try {
            if (!window.indexedDB) {
              isPrivate = true;
            }
          } catch (e) {
            isPrivate = true;
          }
        } else if (window.localStorage && /Safari/.test(window.navigator.userAgent)) {
          try {
            window.localStorage.setItem('test', 1);
          } catch (e) {
            isPrivate = true;
          }

          if (typeof isPrivate === 'undefined') {
            isPrivate = false;
            window.localStorage.removeItem('test');
          }
        }

      } catch (e) {
        isPrivate = false;
      }

      retry(
        function isDone() {
          return typeof isPrivate !== 'undefined' ? true : false;
        },
        function next(isTimeout) {
          callback(isPrivate);
        }
      );
    }

    var isMobile = {
      Android: function() {
        return navigator.userAgent.match(/Android/i);
      },
      BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry|BB10/i);
      },
      iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
      },
      Opera: function() {
        return navigator.userAgent.match(/Opera Mini/i);
      },
      Windows: function() {
        return navigator.userAgent.match(/IEMobile/i);
      },
      any: function() {
        return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
      },
      getOsName: function() {
        var osName = 'Unknown OS';
        if (isMobile.Android()) {
          osName = 'Android';
        }

        if (isMobile.BlackBerry()) {
          osName = 'BlackBerry';
        }

        if (isMobile.iOS()) {
          osName = 'iOS';
        }

        if (isMobile.Opera()) {
          osName = 'Opera Mini';
        }

        if (isMobile.Windows()) {
          osName = 'Windows';
        }

        return osName;
      }
    };

    // via: http://jsfiddle.net/ChristianL/AVyND/
    function detectDesktopOS() {
      var unknown = '-';

      var nVer = navigator.appVersion;
      var nAgt = navigator.userAgent;

      var os = unknown;
      var clientStrings = [{
        s: 'Windows 10',
        r: /(Windows 10.0|Windows NT 10.0)/
      }, {
        s: 'Windows 8.1',
        r: /(Windows 8.1|Windows NT 6.3)/
      }, {
        s: 'Windows 8',
        r: /(Windows 8|Windows NT 6.2)/
      }, {
        s: 'Windows 7',
        r: /(Windows 7|Windows NT 6.1)/
      }, {
        s: 'Windows Vista',
        r: /Windows NT 6.0/
      }, {
        s: 'Windows Server 2003',
        r: /Windows NT 5.2/
      }, {
        s: 'Windows XP',
        r: /(Windows NT 5.1|Windows XP)/
      }, {
        s: 'Windows 2000',
        r: /(Windows NT 5.0|Windows 2000)/
      }, {
        s: 'Windows ME',
        r: /(Win 9x 4.90|Windows ME)/
      }, {
        s: 'Windows 98',
        r: /(Windows 98|Win98)/
      }, {
        s: 'Windows 95',
        r: /(Windows 95|Win95|Windows_95)/
      }, {
        s: 'Windows NT 4.0',
        r: /(Windows NT 4.0|WinNT4.0|WinNT|Windows NT)/
      }, {
        s: 'Windows CE',
        r: /Windows CE/
      }, {
        s: 'Windows 3.11',
        r: /Win16/
      }, {
        s: 'Android',
        r: /Android/
      }, {
        s: 'Open BSD',
        r: /OpenBSD/
      }, {
        s: 'Sun OS',
        r: /SunOS/
      }, {
        s: 'Linux',
        r: /(Linux|X11)/
      }, {
        s: 'iOS',
        r: /(iPhone|iPad|iPod)/
      }, {
        s: 'Mac OS X',
        r: /Mac OS X/
      }, {
        s: 'Mac OS',
        r: /(MacPPC|MacIntel|Mac_PowerPC|Macintosh)/
      }, {
        s: 'QNX',
        r: /QNX/
      }, {
        s: 'UNIX',
        r: /UNIX/
      }, {
        s: 'BeOS',
        r: /BeOS/
      }, {
        s: 'OS/2',
        r: /OS\/2/
      }, {
        s: 'Search Bot',
        r: /(nuhk|Googlebot|Yammybot|Openbot|Slurp|MSNBot|Ask Jeeves\/Teoma|ia_archiver)/
      }];
      for (var i = 0, cs; cs = clientStrings[i]; i++) {
        if (cs.r.test(nAgt)) {
          os = cs.s;
          break;
        }
      }

      var osVersion = unknown;

      if (/Windows/.test(os)) {
        if (/Windows (.*)/.test(os)) {
          osVersion = /Windows (.*)/.exec(os)[1];
        }
        os = 'Windows';
      }

      switch (os) {
      case 'Mac OS X':
        if (/Mac OS X (10[\.\_\d]+)/.test(nAgt)) {
          osVersion = /Mac OS X (10[\.\_\d]+)/.exec(nAgt)[1];
        }
        break;
      case 'Android':
        if (/Android ([\.\_\d]+)/.test(nAgt)) {
          osVersion = /Android ([\.\_\d]+)/.exec(nAgt)[1];
        }
        break;
      case 'iOS':
        if (/OS (\d+)_(\d+)_?(\d+)?/.test(nAgt)) {
          osVersion = /OS (\d+)_(\d+)_?(\d+)?/.exec(nVer);
          osVersion = osVersion[1] + '.' + osVersion[2] + '.' + (osVersion[3] | 0);
        }
        break;
      }

      return {
        osName: os,
        osVersion: osVersion
      };
    }

    var osName = 'Unknown OS';
    var osVersion = 'Unknown OS Version';

    function getAndroidVersion(ua) {
      ua = (ua || navigator.userAgent).toLowerCase();
      var match = ua.match(/android\s([0-9\.]*)/);
      return match ? match[1] : false;
    }

    var osInfo = detectDesktopOS();

    if (osInfo && osInfo.osName && osInfo.osName != '-') {
      osName = osInfo.osName;
      osVersion = osInfo.osVersion;
    } else if (isMobile.any()) {
      osName = isMobile.getOsName();

      if (osName == 'Android') {
        osVersion = getAndroidVersion();
      }
    }

    var isNodejs = typeof process === 'object' && typeof process.versions === 'object' && process.versions.node;

    if (osName === 'Unknown OS' && isNodejs) {
      osName = 'Nodejs';
      osVersion = process.versions.node.toString().replace('v', '');
    }

    var isCanvasSupportsStreamCapturing = false;
    var isVideoSupportsStreamCapturing = false;
    ['captureStream', 'mozCaptureStream', 'webkitCaptureStream'].forEach(function(item) {
      if (typeof document === 'undefined' || typeof document.createElement !== 'function') {
        return;
      }

      if (!isCanvasSupportsStreamCapturing && item in document.createElement('canvas')) {
        isCanvasSupportsStreamCapturing = true;
      }

      if (!isVideoSupportsStreamCapturing && item in document.createElement('video')) {
        isVideoSupportsStreamCapturing = true;
      }
    });

    var regexIpv4Local = /^(192\.168\.|169\.254\.|10\.|172\.(1[6-9]|2\d|3[01]))/,
      regexIpv4 = /([0-9]{1,3}(\.[0-9]{1,3}){3})/,
      regexIpv6 = /[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7}/;

    // via: https://github.com/diafygi/webrtc-ips
    function DetectLocalIPAddress(callback, stream) {
      if (!DetectRTC.isWebRTCSupported) {
        return;
      }

      var isPublic = true,
        isIpv4 = true;
      getIPs(function(ip) {
        if (!ip) {
          callback(); // Pass nothing to tell that ICE-gathering-ended
        } else if (ip.match(regexIpv4Local)) {
          isPublic = false;
          callback('Local: ' + ip, isPublic, isIpv4);
        } else if (ip.match(regexIpv6)) { //via https://ourcodeworld.com/articles/read/257/how-to-get-the-client-ip-address-with-javascript-only
          isIpv4 = false;
          callback('Public: ' + ip, isPublic, isIpv4);
        } else {
          callback('Public: ' + ip, isPublic, isIpv4);
        }
      }, stream);
    }

    function getIPs(callback, stream) {
      if (typeof document === 'undefined' || typeof document.getElementById !== 'function') {
        return;
      }

      var ipDuplicates = {};

      var RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;

      if (!RTCPeerConnection) {
        var iframe = document.getElementById('iframe');
        if (!iframe) {
          return;
        }
        var win = iframe.contentWindow;
        RTCPeerConnection = win.RTCPeerConnection || win.mozRTCPeerConnection || win.webkitRTCPeerConnection;
      }

      if (!RTCPeerConnection) {
        return;
      }

      var peerConfig = null;

      if (DetectRTC.browser === 'Chrome' && DetectRTC.browser.version < 58) {
        // todo: add support for older Opera
        peerConfig = {
          optional: [{
            RtpDataChannels: true
          }]
        };
      }

      var servers = {
        iceServers: [{
          urls: 'stun:stun.l.google.com:19302'
        }]
      };

      var pc = new RTCPeerConnection(servers, peerConfig);

      if (stream) {
        if (pc.addStream) {
          pc.addStream(stream);
        } else if (pc.addTrack && stream.getTracks()[0]) {
          pc.addTrack(stream.getTracks()[0], stream);
        }
      }

      function handleCandidate(candidate) {
        if (!candidate) {
          callback(); // Pass nothing to tell that ICE-gathering-ended
          return;
        }

        var match = regexIpv4.exec(candidate);
        if (!match) {
          return;
        }
        var ipAddress = match[1];
        var isPublic = (candidate.match(regexIpv4Local)),
          isIpv4 = true;

        if (ipDuplicates[ipAddress] === undefined) {
          callback(ipAddress, isPublic, isIpv4);
        }

        ipDuplicates[ipAddress] = true;
      }

      // listen for candidate events
      pc.onicecandidate = function(event) {
        if (event.candidate && event.candidate.candidate) {
          handleCandidate(event.candidate.candidate);
        } else {
          handleCandidate(); // Pass nothing to tell that ICE-gathering-ended
        }
      };

      // create data channel
      if (!stream) {
        try {
          pc.createDataChannel('sctp', {});
        } catch (e) {}
      }

      // create an offer sdp
      if (DetectRTC.isPromisesSupported) {
        pc.createOffer().then(function(result) {
          pc.setLocalDescription(result).then(afterCreateOffer);
        });
      } else {
        pc.createOffer(function(result) {
          pc.setLocalDescription(result, afterCreateOffer, function() {});
        }, function() {});
      }

      function afterCreateOffer() {
        var lines = pc.localDescription.sdp.split('\n');

        lines.forEach(function(line) {
          if (line && line.indexOf('a=candidate:') === 0) {
            handleCandidate(line);
          }
        });
      }
    }

    var MediaDevices = [];

    var audioInputDevices = [];
    var audioOutputDevices = [];
    var videoInputDevices = [];

    if (navigator.mediaDevices && navigator.mediaDevices.enumerateDevices) {
      // Firefox 38+ seems having support of enumerateDevices
      // Thanks @xdumaine/enumerateDevices
      navigator.enumerateDevices = function(callback) {
        var enumerateDevices = navigator.mediaDevices.enumerateDevices();
        if (enumerateDevices && enumerateDevices.then) {
          navigator.mediaDevices.enumerateDevices().then(callback).catch(function() {
            callback([]);
          });
        } else {
          callback([]);
        }
      };
    }

    // Media Devices detection
    var canEnumerate = false;

    /*global MediaStreamTrack:true */
    if (typeof MediaStreamTrack !== 'undefined' && 'getSources' in MediaStreamTrack) {
      canEnumerate = true;
    } else if (navigator.mediaDevices && !!navigator.mediaDevices.enumerateDevices) {
      canEnumerate = true;
    }

    var hasMicrophone = false;
    var hasSpeakers = false;
    var hasWebcam = false;

    var isWebsiteHasMicrophonePermissions = false;
    var isWebsiteHasWebcamPermissions = false;

    // http://dev.w3.org/2011/webrtc/editor/getusermedia.html#mediadevices
    function checkDeviceSupport(callback) {
      if (!canEnumerate) {
        if (callback) {
          callback();
        }
        return;
      }

      if (!navigator.enumerateDevices && window.MediaStreamTrack && window.MediaStreamTrack.getSources) {
        navigator.enumerateDevices = window.MediaStreamTrack.getSources.bind(window.MediaStreamTrack);
      }

      if (!navigator.enumerateDevices && navigator.enumerateDevices) {
        navigator.enumerateDevices = navigator.enumerateDevices.bind(navigator);
      }

      if (!navigator.enumerateDevices) {
        if (callback) {
          callback();
        }
        return;
      }

      MediaDevices = [];

      audioInputDevices = [];
      audioOutputDevices = [];
      videoInputDevices = [];

      hasMicrophone = false;
      hasSpeakers = false;
      hasWebcam = false;

      isWebsiteHasMicrophonePermissions = false;
      isWebsiteHasWebcamPermissions = false;

      // to prevent duplication
      var alreadyUsedDevices = {};

      navigator.enumerateDevices(function(devices) {
        devices.forEach(function(_device) {
          var device = {};
          for (var d in _device) {
            try {
              if (typeof _device[d] !== 'function') {
                device[d] = _device[d];
              }
            } catch (e) {}
          }

          if (alreadyUsedDevices[device.deviceId + device.label + device.kind]) {
            return;
          }

          // if it is MediaStreamTrack.getSources
          if (device.kind === 'audio') {
            device.kind = 'audioinput';
          }

          if (device.kind === 'video') {
            device.kind = 'videoinput';
          }

          if (!device.deviceId) {
            device.deviceId = device.id;
          }

          if (!device.id) {
            device.id = device.deviceId;
          }

          if (!device.label) {
            device.isCustomLabel = true;

            if (device.kind === 'videoinput') {
              device.label = 'Camera ' + (videoInputDevices.length + 1);
            } else if (device.kind === 'audioinput') {
              device.label = 'Microphone ' + (audioInputDevices.length + 1);
            } else if (device.kind === 'audiooutput') {
              device.label = 'Speaker ' + (audioOutputDevices.length + 1);
            } else {
              device.label = 'Please invoke getUserMedia once.';
            }

            if (typeof DetectRTC !== 'undefined' && DetectRTC.browser.isChrome && DetectRTC.browser.version >= 46 && !/^(https:|chrome-extension:)$/g.test(location.protocol || '')) {
              if (typeof document !== 'undefined' && typeof document.domain === 'string' && document.domain.search && document.domain.search(/localhost|127.0./g) === -1) {
                device.label = 'HTTPs is required to get label of this ' + device.kind + ' device.';
              }
            }
          } else {
            // Firefox on Android still returns empty label
            if (device.kind === 'videoinput' && !isWebsiteHasWebcamPermissions) {
              isWebsiteHasWebcamPermissions = true;
            }

            if (device.kind === 'audioinput' && !isWebsiteHasMicrophonePermissions) {
              isWebsiteHasMicrophonePermissions = true;
            }
          }

          if (device.kind === 'audioinput') {
            hasMicrophone = true;

            if (audioInputDevices.indexOf(device) === -1) {
              audioInputDevices.push(device);
            }
          }

          if (device.kind === 'audiooutput') {
            hasSpeakers = true;

            if (audioOutputDevices.indexOf(device) === -1) {
              audioOutputDevices.push(device);
            }
          }

          if (device.kind === 'videoinput') {
            hasWebcam = true;

            if (videoInputDevices.indexOf(device) === -1) {
              videoInputDevices.push(device);
            }
          }

          // there is no 'videoouput' in the spec.
          MediaDevices.push(device);

          alreadyUsedDevices[device.deviceId + device.label + device.kind] = device;
        });

        if (typeof DetectRTC !== 'undefined') {
          // to sync latest outputs
          DetectRTC.MediaDevices = MediaDevices;
          DetectRTC.hasMicrophone = hasMicrophone;
          DetectRTC.hasSpeakers = hasSpeakers;
          DetectRTC.hasWebcam = hasWebcam;

          DetectRTC.isWebsiteHasWebcamPermissions = isWebsiteHasWebcamPermissions;
          DetectRTC.isWebsiteHasMicrophonePermissions = isWebsiteHasMicrophonePermissions;

          DetectRTC.audioInputDevices = audioInputDevices;
          DetectRTC.audioOutputDevices = audioOutputDevices;
          DetectRTC.videoInputDevices = videoInputDevices;
        }

        if (callback) {
          callback();
        }
      });
    }

    var DetectRTC = window.DetectRTC || {};

    // ----------
    // DetectRTC.browser.name || DetectRTC.browser.version || DetectRTC.browser.fullVersion
    DetectRTC.browser = getBrowserInfo();

    detectPrivateMode(function(isPrivateBrowsing) {
      DetectRTC.browser.isPrivateBrowsing = !!isPrivateBrowsing;
    });

    // DetectRTC.isChrome || DetectRTC.isFirefox || DetectRTC.isEdge
    DetectRTC.browser['is' + DetectRTC.browser.name] = true;

    // -----------
    DetectRTC.osName = osName;
    DetectRTC.osVersion = osVersion;

    var isNodeWebkit = typeof process === 'object' && typeof process.versions === 'object' && process.versions['node-webkit'];

    // --------- Detect if system supports WebRTC 1.0 or WebRTC 1.1.
    var isWebRTCSupported = false;
    ['RTCPeerConnection', 'webkitRTCPeerConnection', 'mozRTCPeerConnection', 'RTCIceGatherer'].forEach(function(item) {
      if (isWebRTCSupported) {
        return;
      }

      if (item in window) {
        isWebRTCSupported = true;
      }
    });
    DetectRTC.isWebRTCSupported = isWebRTCSupported;

    //-------
    DetectRTC.isORTCSupported = typeof RTCIceGatherer !== 'undefined';

    // --------- Detect if system supports screen capturing API
    var isScreenCapturingSupported = false;
    if (DetectRTC.browser.isChrome && DetectRTC.browser.version >= 35) {
      isScreenCapturingSupported = true;
    } else if (DetectRTC.browser.isFirefox && DetectRTC.browser.version >= 34) {
      isScreenCapturingSupported = true;
    } else if (DetectRTC.browser.isEdge && DetectRTC.browser.version >= 17) {
      isScreenCapturingSupported = true; // navigator.getDisplayMedia
    } else if (DetectRTC.osName === 'Android' && DetectRTC.browser.isChrome) {
      isScreenCapturingSupported = true;
    }

    if (!/^(https:|chrome-extension:)$/g.test(location.protocol || '')) {
      var isNonLocalHost = typeof document !== 'undefined' && typeof document.domain === 'string' && document.domain.search && document.domain.search(/localhost|127.0./g) === -1;
      if (isNonLocalHost && (DetectRTC.browser.isChrome || DetectRTC.browser.isEdge || DetectRTC.browser.isOpera)) {
        isScreenCapturingSupported = false;
      } else if (DetectRTC.browser.isFirefox) {
        isScreenCapturingSupported = false;
      }
    }
    DetectRTC.isScreenCapturingSupported = isScreenCapturingSupported;

    // --------- Detect if WebAudio API are supported
    var webAudio = {
      isSupported: false,
      isCreateMediaStreamSourceSupported: false
    };

    ['AudioContext', 'webkitAudioContext', 'mozAudioContext', 'msAudioContext'].forEach(function(item) {
      if (webAudio.isSupported) {
        return;
      }

      if (item in window) {
        webAudio.isSupported = true;

        if (window[item] && 'createMediaStreamSource' in window[item].prototype) {
          webAudio.isCreateMediaStreamSourceSupported = true;
        }
      }
    });
    DetectRTC.isAudioContextSupported = webAudio.isSupported;
    DetectRTC.isCreateMediaStreamSourceSupported = webAudio.isCreateMediaStreamSourceSupported;

    // ---------- Detect if SCTP/RTP channels are supported.

    var isRtpDataChannelsSupported = false;
    if (DetectRTC.browser.isChrome && DetectRTC.browser.version > 31) {
      isRtpDataChannelsSupported = true;
    }
    DetectRTC.isRtpDataChannelsSupported = isRtpDataChannelsSupported;

    var isSCTPSupportd = false;
    if (DetectRTC.browser.isFirefox && DetectRTC.browser.version > 28) {
      isSCTPSupportd = true;
    } else if (DetectRTC.browser.isChrome && DetectRTC.browser.version > 25) {
      isSCTPSupportd = true;
    } else if (DetectRTC.browser.isOpera && DetectRTC.browser.version >= 11) {
      isSCTPSupportd = true;
    }
    DetectRTC.isSctpDataChannelsSupported = isSCTPSupportd;

    // ---------

    DetectRTC.isMobileDevice = isMobileDevice; // "isMobileDevice" boolean is defined in "getBrowserInfo.js"

    // ------
    var isGetUserMediaSupported = false;
    if (navigator.getUserMedia) {
      isGetUserMediaSupported = true;
    } else if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
      isGetUserMediaSupported = true;
    }

    if (DetectRTC.browser.isChrome && DetectRTC.browser.version >= 46 && !/^(https:|chrome-extension:)$/g.test(location.protocol || '')) {
      if (typeof document !== 'undefined' && typeof document.domain === 'string' && document.domain.search && document.domain.search(/localhost|127.0./g) === -1) {
        isGetUserMediaSupported = 'Requires HTTPs';
      }
    }

    if (DetectRTC.osName === 'Nodejs') {
      isGetUserMediaSupported = false;
    }
    DetectRTC.isGetUserMediaSupported = isGetUserMediaSupported;

    var displayResolution = '';
    if (screen.width) {
      var width = (screen.width) ? screen.width : '';
      var height = (screen.height) ? screen.height : '';
      displayResolution += '' + width + ' x ' + height;
    }
    DetectRTC.displayResolution = displayResolution;

    function getAspectRatio(w, h) {
      function gcd(a, b) {
        return (b == 0) ? a : gcd(b, a % b);
      }
      var r = gcd(w, h);
      return (w / r) / (h / r);
    }

    DetectRTC.displayAspectRatio = getAspectRatio(screen.width, screen.height).toFixed(2);

    // ----------
    DetectRTC.isCanvasSupportsStreamCapturing = isCanvasSupportsStreamCapturing;
    DetectRTC.isVideoSupportsStreamCapturing = isVideoSupportsStreamCapturing;

    if (DetectRTC.browser.name == 'Chrome' && DetectRTC.browser.version >= 53) {
      if (!DetectRTC.isCanvasSupportsStreamCapturing) {
        DetectRTC.isCanvasSupportsStreamCapturing = 'Requires chrome flag: enable-experimental-web-platform-features';
      }

      if (!DetectRTC.isVideoSupportsStreamCapturing) {
        DetectRTC.isVideoSupportsStreamCapturing = 'Requires chrome flag: enable-experimental-web-platform-features';
      }
    }

    // ------
    DetectRTC.DetectLocalIPAddress = DetectLocalIPAddress;

    DetectRTC.isWebSocketsSupported = 'WebSocket' in window && 2 === window.WebSocket.CLOSING;
    DetectRTC.isWebSocketsBlocked = !DetectRTC.isWebSocketsSupported;

    if (DetectRTC.osName === 'Nodejs') {
      DetectRTC.isWebSocketsSupported = true;
      DetectRTC.isWebSocketsBlocked = false;
    }

    DetectRTC.checkWebSocketsSupport = function(callback) {
      callback = callback || function() {};
      try {
        var starttime;
        var websocket = new WebSocket('wss://echo.websocket.org:443/');
        websocket.onopen = function() {
          DetectRTC.isWebSocketsBlocked = false;
          starttime = (new Date).getTime();
          websocket.send('ping');
        };
        websocket.onmessage = function() {
          DetectRTC.WebsocketLatency = (new Date).getTime() - starttime + 'ms';
          callback();
          websocket.close();
          websocket = null;
        };
        websocket.onerror = function() {
          DetectRTC.isWebSocketsBlocked = true;
          callback();
        };
      } catch (e) {
        DetectRTC.isWebSocketsBlocked = true;
        callback();
      }
    };

    // -------
    DetectRTC.load = function(callback) {
      callback = callback || function() {};
      checkDeviceSupport(callback);
    };

    // check for microphone/camera support!
    if (typeof checkDeviceSupport === 'function') {
      // checkDeviceSupport();
    }

    if (typeof MediaDevices !== 'undefined') {
      DetectRTC.MediaDevices = MediaDevices;
    } else {
      DetectRTC.MediaDevices = [];
    }

    DetectRTC.hasMicrophone = hasMicrophone;
    DetectRTC.hasSpeakers = hasSpeakers;
    DetectRTC.hasWebcam = hasWebcam;

    DetectRTC.isWebsiteHasWebcamPermissions = isWebsiteHasWebcamPermissions;
    DetectRTC.isWebsiteHasMicrophonePermissions = isWebsiteHasMicrophonePermissions;

    DetectRTC.audioInputDevices = audioInputDevices;
    DetectRTC.audioOutputDevices = audioOutputDevices;
    DetectRTC.videoInputDevices = videoInputDevices;

    // ------
    var isSetSinkIdSupported = false;
    if (typeof document !== 'undefined' && typeof document.createElement === 'function' && 'setSinkId' in document.createElement('video')) {
      isSetSinkIdSupported = true;
    }
    DetectRTC.isSetSinkIdSupported = isSetSinkIdSupported;

    // -----
    var isRTPSenderReplaceTracksSupported = false;
    if (DetectRTC.browser.isFirefox && typeof mozRTCPeerConnection !== 'undefined' /*&& DetectRTC.browser.version > 39*/ ) {
      /*global mozRTCPeerConnection:true */
      if ('getSenders' in mozRTCPeerConnection.prototype) {
        isRTPSenderReplaceTracksSupported = true;
      }
    } else if (DetectRTC.browser.isChrome && typeof webkitRTCPeerConnection !== 'undefined') {
      /*global webkitRTCPeerConnection:true */
      if ('getSenders' in webkitRTCPeerConnection.prototype) {
        isRTPSenderReplaceTracksSupported = true;
      }
    }
    DetectRTC.isRTPSenderReplaceTracksSupported = isRTPSenderReplaceTracksSupported;

    //------
    var isRemoteStreamProcessingSupported = false;
    if (DetectRTC.browser.isFirefox && DetectRTC.browser.version > 38) {
      isRemoteStreamProcessingSupported = true;
    }
    DetectRTC.isRemoteStreamProcessingSupported = isRemoteStreamProcessingSupported;

    //-------
    var isApplyConstraintsSupported = false;

    /*global MediaStreamTrack:true */
    if (typeof MediaStreamTrack !== 'undefined' && 'applyConstraints' in MediaStreamTrack.prototype) {
      isApplyConstraintsSupported = true;
    }
    DetectRTC.isApplyConstraintsSupported = isApplyConstraintsSupported;

    //-------
    var isMultiMonitorScreenCapturingSupported = false;
    if (DetectRTC.browser.isFirefox && DetectRTC.browser.version >= 43) {
      // version 43 merely supports platforms for multi-monitors
      // version 44 will support exact multi-monitor selection i.e. you can select any monitor for screen capturing.
      isMultiMonitorScreenCapturingSupported = true;
    }
    DetectRTC.isMultiMonitorScreenCapturingSupported = isMultiMonitorScreenCapturingSupported;

    DetectRTC.isPromisesSupported = !!('Promise' in window);

    // version is generated by "grunt"
    DetectRTC.version = '1.3.9';

    if (typeof DetectRTC === 'undefined') {
      window.DetectRTC = {};
    }

    var MediaStream = window.MediaStream;

    if (typeof MediaStream === 'undefined' && typeof webkitMediaStream !== 'undefined') {
      MediaStream = webkitMediaStream;
    }

    if (typeof MediaStream !== 'undefined' && typeof MediaStream === 'function') {
      DetectRTC.MediaStream = Object.keys(MediaStream.prototype);
    } else DetectRTC.MediaStream = false;

    if (typeof MediaStreamTrack !== 'undefined') {
      DetectRTC.MediaStreamTrack = Object.keys(MediaStreamTrack.prototype);
    } else DetectRTC.MediaStreamTrack = false;

    var RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;

    if (typeof RTCPeerConnection !== 'undefined') {
      DetectRTC.RTCPeerConnection = Object.keys(RTCPeerConnection.prototype);
    } else DetectRTC.RTCPeerConnection = false;

    window.DetectRTC = DetectRTC;

    if (typeof module !== 'undefined' /* && !!module.exports*/ ) {
      module.exports = DetectRTC;
    }

    if (typeof define === 'function' && define.amd) {
      define('DetectRTC', [], function() {
        return DetectRTC;
      });
    }
  })();

  // globals.js

  if (typeof cordova !== 'undefined') {
    DetectRTC.isMobileDevice = true;
    DetectRTC.browser.name = 'Chrome';
  }

  if (navigator && navigator.userAgent && navigator.userAgent.indexOf('Crosswalk') !== -1) {
    DetectRTC.isMobileDevice = true;
    DetectRTC.browser.name = 'Chrome';
  }

  function fireEvent(obj, eventName, args) {
    if (typeof CustomEvent === 'undefined') {
      return;
    }

    var eventDetail = {
      arguments: args,
      __exposedProps__: args
    };

    var event = new CustomEvent(eventName, eventDetail);
    obj.dispatchEvent(event);
  }

  function setHarkEvents(broadcast, streamEvent) {
    if (!streamEvent.stream || !getTracks(streamEvent.stream, 'audio').length) return;

    if (!broadcast || !streamEvent) {
      throw 'Both arguments are required.';
    }

    if (!broadcast.onspeaking || !broadcast.onsilence) {
      return;
    }

    if (typeof hark === 'undefined') {
      throw 'hark.js not found.';
    }

    hark(streamEvent.stream, {
      onspeaking: function() {
        broadcast.onspeaking(streamEvent);
      },
      onsilence: function() {
        broadcast.onsilence(streamEvent);
      },
      onvolumechange: function(volume, threshold) {
        if (!broadcast.onvolumechange) {
          return;
        }
        broadcast.onvolumechange(merge({
          volume: volume,
          threshold: threshold
        }, streamEvent));
      }
    });
  }

  function setMuteHandlers(broadcast, streamEvent) {
    if (!streamEvent.stream || !streamEvent.stream || !streamEvent.stream.addEventListener) return;

    streamEvent.stream.addEventListener('mute', function(event) {
      event = broadcast.streamEvents[streamEvent.streamid];

      event.session = {
        audio: event.muteType === 'audio',
        video: event.muteType === 'video'
      };

      broadcast.onmute(event);
    }, false);

    streamEvent.stream.addEventListener('unmute', function(event) {
      event = broadcast.streamEvents[streamEvent.streamid];

      event.session = {
        audio: event.unmuteType === 'audio',
        video: event.unmuteType === 'video'
      };

      broadcast.onunmute(event);
    }, false);
  }

  function getRandomString() {
    if (window.crypto && window.crypto.getRandomValues && navigator.userAgent.indexOf('Safari') === -1) {
      var a = window.crypto.getRandomValues(new Uint32Array(3)),
        token = '';
      for (var i = 0, l = a.length; i < l; i++) {
        token += a[i].toString(36);
      }
      return token;
    } else {
      return (Math.random() * new Date().getTime()).toString(36).replace(/\./g, '');
    }
  }

  // Get HTMLAudioElement/HTMLVideoElement accordingly
  // todo: add API documentation for broadcast.autoCreateMediaElement

  function getRMCMediaElement(stream, callback, broadcast) {
    if (!broadcast.autoCreateMediaElement) {
      callback({});
      return;
    }

    var isAudioOnly = false;
    if (!getTracks(stream, 'video').length && !stream.isVideo && !stream.isScreen) {
      isAudioOnly = true;
    }

    if (DetectRTC.browser.name === 'Firefox') {
      if (broadcast.session.video || broadcast.session.screen) {
        isAudioOnly = false;
      }
    }

    var mediaElement = document.createElement(isAudioOnly ? 'audio' : 'video');

    mediaElement.srcObject = stream;

    mediaElement.setAttribute('autoplay', true);
    mediaElement.setAttribute('playsinline', false);
    mediaElement.setAttribute('controls', true);
    mediaElement.setAttribute('muted', false);
    mediaElement.setAttribute('volume', 1);

    // http://goo.gl/WZ5nFl
    // Firefox don't yet support onended for any stream (remote/local)
    if (DetectRTC.browser.name === 'Firefox') {
      var streamEndedEvent = 'ended';

      if ('oninactive' in mediaElement) {
        streamEndedEvent = 'inactive';
      }

      mediaElement.addEventListener(streamEndedEvent, function() {
        // fireEvent(stream, streamEndedEvent, stream);
        currentUserMediaRequest.remove(stream.idInstance);

        if (stream.type === 'local') {
          streamEndedEvent = 'ended';

          if ('oninactive' in stream) {
            streamEndedEvent = 'inactive';
          }

          StreamsHandler.onSyncNeeded(stream.streamid, streamEndedEvent);

          broadcast.attachStreams.forEach(function(aStream, idx) {
            if (stream.streamid === aStream.streamid) {
              delete broadcast.attachStreams[idx];
            }
          });

          var newStreamsArray = [];
          broadcast.attachStreams.forEach(function(aStream) {
            if (aStream) {
              newStreamsArray.push(aStream);
            }
          });
          broadcast.attachStreams = newStreamsArray;

          var streamEvent = broadcast.streamEvents[stream.streamid];

          if (streamEvent) {
            broadcast.onstreamended(streamEvent);
            return;
          }
          if (this.parentNode) {
            this.parentNode.removeChild(this);
          }
        }
      }, false);
    }

    var played = mediaElement.play();
    if (typeof played !== 'undefined') {
      var cbFired = false;
      setTimeout(function() {
        if (!cbFired) {
          cbFired = true;
          callback(mediaElement);
        }
      }, 1000);
      played.then(function() {
        if (cbFired) return;
        cbFired = true;
        callback(mediaElement);
      }).catch(function(error) {
        if (cbFired) return;
        cbFired = true;
        callback(mediaElement);
      });
    } else {
      callback(mediaElement);
    }
  }

  // if IE
  if (!window.addEventListener) {
    window.addEventListener = function(el, eventName, eventHandler) {
      if (!el.attachEvent) {
        return;
      }
      el.attachEvent('on' + eventName, eventHandler);
    };
  }

  function listenEventHandler(eventName, eventHandler) {
    window.removeEventListener(eventName, eventHandler);
    window.addEventListener(eventName, eventHandler, false);
  }

  window.attachEventListener = function(video, type, listener, useCapture) {
    video.addEventListener(type, listener, useCapture);
  };

  function removeNullEntries(array) {
    var newArray = [];
    array.forEach(function(item) {
      if (item) {
        newArray.push(item);
      }
    });
    return newArray;
  }


  function isData(session) {
    return !session.audio && !session.video && !session.screen && session.data;
  }

  function isNull(obj) {
    return typeof obj === 'undefined';
  }

  function isString(obj) {
    return typeof obj === 'string';
  }

  var MediaStream = window.MediaStream;

  if (typeof MediaStream === 'undefined' && typeof webkitMediaStream !== 'undefined') {
    MediaStream = webkitMediaStream;
  }

  /*global MediaStream:true */
  if (typeof MediaStream !== 'undefined') {
    if (!('stop' in MediaStream.prototype)) {
      MediaStream.prototype.stop = function() {
        this.getTracks().forEach(function(track) {
          track.stop();
        });
      };
    }
  }

  function isAudioPlusTab(broadcast, audioPlusTab) {
    if (broadcast.session.audio && broadcast.session.audio === 'two-way') {
      return false;
    }

    if (DetectRTC.browser.name === 'Firefox' && audioPlusTab !== false) {
      return true;
    }

    if (DetectRTC.browser.name !== 'Chrome' || DetectRTC.browser.version < 50) return false;

    if (typeof audioPlusTab === true) {
      return true;
    }

    if (typeof audioPlusTab === 'undefined' && broadcast.session.audio && broadcast.session.screen && !broadcast.session.video) {
      audioPlusTab = true;
      return true;
    }

    return false;
  }

  function getAudioScreenConstraints(screen_constraints) {
    if (DetectRTC.browser.name === 'Firefox') {
      return true;
    }

    if (DetectRTC.browser.name !== 'Chrome') return false;

    return {
      mandatory: {
        chromeMediaSource: screen_constraints.mandatory.chromeMediaSource,
        chromeMediaSourceId: screen_constraints.mandatory.chromeMediaSourceId
      }
    };
  }

  window.iOSDefaultAudioOutputDevice = window.iOSDefaultAudioOutputDevice || 'speaker'; // earpiece or speaker

  function getTracks(stream, kind) {
    if (!stream || !stream.getTracks) {
      return [];
    }

    return stream.getTracks().filter(function(t) {
      return t.kind === (kind || 'audio');
    });
  }

  function isUnifiedPlanSupportedDefault() {
    var canAddTransceiver = false;

    try {
      if (typeof RTCRtpTransceiver === 'undefined') return false;
      if (!('currentDirection' in RTCRtpTransceiver.prototype)) return false;

      var tempPc = new RTCPeerConnection();

      try {
        tempPc.addTransceiver('audio');
        canAddTransceiver = true;
      } catch (e) {}

      tempPc.close();
    } catch (e) {
      canAddTransceiver = false;
    }

    return canAddTransceiver && isUnifiedPlanSuppored();
  }

  function isUnifiedPlanSuppored() {
    var isUnifiedPlanSupported = false;

    try {
      var pc = new RTCPeerConnection({
        sdpSemantics: 'unified-plan'
      });

      try {
        var config = pc.getConfiguration();
        if (config.sdpSemantics == 'unified-plan')
          isUnifiedPlanSupported = true;
        else if (config.sdpSemantics == 'plan-b')
          isUnifiedPlanSupported = false;
        else
          isUnifiedPlanSupported = false;
      } catch (e) {
        isUnifiedPlanSupported = false;
      }
    } catch (e) {
      isUnifiedPlanSupported = false;
    }

    return isUnifiedPlanSupported;
  }

  // ios-hacks.js

  function setCordovaAPIs() {
    // if (DetectRTC.osName !== 'iOS') return;
    if (typeof cordova === 'undefined' || typeof cordova.plugins === 'undefined' || typeof cordova.plugins.iosrtc === 'undefined') return;

    var iosrtc = cordova.plugins.iosrtc;
    window.webkitRTCPeerConnection = iosrtc.RTCPeerConnection;
    window.RTCSessionDescription = iosrtc.RTCSessionDescription;
    window.RTCIceCandidate = iosrtc.RTCIceCandidate;
    window.MediaStream = iosrtc.MediaStream;
    window.MediaStreamTrack = iosrtc.MediaStreamTrack;
    navigator.getUserMedia = navigator.webkitGetUserMedia = iosrtc.getUserMedia;

    iosrtc.debug.enable('iosrtc*');
    if (typeof iosrtc.selectAudioOutput == 'function') {
      iosrtc.selectAudioOutput(window.iOSDefaultAudioOutputDevice || 'speaker'); // earpiece or speaker
    }
    iosrtc.registerGlobals();
  }

  document.addEventListener('deviceready', setCordovaAPIs, false);
  setCordovaAPIs();

  // RTCPeerConnection.js

  var defaults = {};

  function setSdpConstraints(config) {
    var sdpConstraints = {
      OfferToReceiveAudio: !!config.OfferToReceiveAudio,
      OfferToReceiveVideo: !!config.OfferToReceiveVideo
    };

    return sdpConstraints;
  }

  var RTCPeerConnection;
  if (typeof window.RTCPeerConnection !== 'undefined') {
    RTCPeerConnection = window.RTCPeerConnection;
  } else if (typeof mozRTCPeerConnection !== 'undefined') {
    RTCPeerConnection = mozRTCPeerConnection;
  } else if (typeof webkitRTCPeerConnection !== 'undefined') {
    RTCPeerConnection = webkitRTCPeerConnection;
  }

  var RTCSessionDescription = window.RTCSessionDescription || window.mozRTCSessionDescription;
  var RTCIceCandidate = window.RTCIceCandidate || window.mozRTCIceCandidate;
  var MediaStreamTrack = window.MediaStreamTrack;

  function PeerInitiator(config) {
    if (typeof window.RTCPeerConnection !== 'undefined') {
      RTCPeerConnection = window.RTCPeerConnection;
    } else if (typeof mozRTCPeerConnection !== 'undefined') {
      RTCPeerConnection = mozRTCPeerConnection;
    } else if (typeof webkitRTCPeerConnection !== 'undefined') {
      RTCPeerConnection = webkitRTCPeerConnection;
    }

    RTCSessionDescription = window.RTCSessionDescription || window.mozRTCSessionDescription;
    RTCIceCandidate = window.RTCIceCandidate || window.mozRTCIceCandidate;
    MediaStreamTrack = window.MediaStreamTrack;

    if (!RTCPeerConnection) {
      throw 'WebRTC 1.0 (RTCPeerConnection) API are NOT available in this browser.';
    }

    var broadcast = config.rtcMultiConnection;

    this.extra = config.remoteSdp ? config.remoteSdp.extra : broadcast.extra;
    this.userid = config.userid;
    this.streams = [];
    this.channels = config.channels || [];
    this.broadcastDescription = config.broadcastDescription;

    this.addStream = function(session) {
      broadcast.addStream(session, self.userid);
    };

    this.removeStream = function(streamid) {
      broadcast.removeStream(streamid, self.userid);
    };

    var self = this;

    if (config.remoteSdp) {
      this.broadcastDescription = config.remoteSdp.broadcastDescription;
    }

    var allRemoteStreams = {};

    defaults.sdpConstraints = setSdpConstraints({
      OfferToReceiveAudio: true,
      OfferToReceiveVideo: true
    });

    var peer;

    var renegotiatingPeer = !!config.renegotiatingPeer;
    if (config.remoteSdp) {
      renegotiatingPeer = !!config.remoteSdp.renegotiatingPeer;
    }

    var localStreams = [];
    broadcast.attachStreams.forEach(function(stream) {
      if (stream) {
        localStreams.push(stream);
      }
    });

    if (!renegotiatingPeer) {
      var iceTransports = 'all';
      if (broadcast.candidates.turn || broadcast.candidates.relay) {
        if (!broadcast.candidates.stun && !broadcast.candidates.reflexive && !broadcast.candidates.host) {
          iceTransports = 'relay';
        }
      }

      try {
        // ref: developer.mozilla.org/en-US/docs/Web/API/RTCConfiguration
        var params = {
          iceServers: broadcast.iceServers,
          iceTransportPolicy: broadcast.iceTransportPolicy || iceTransports
        };

        if (typeof broadcast.iceCandidatePoolSize !== 'undefined') {
          params.iceCandidatePoolSize = broadcast.iceCandidatePoolSize;
        }

        if (typeof broadcast.bundlePolicy !== 'undefined') {
          params.bundlePolicy = broadcast.bundlePolicy;
        }

        if (typeof broadcast.rtcpMuxPolicy !== 'undefined') {
          params.rtcpMuxPolicy = broadcast.rtcpMuxPolicy;
        }

        if (broadcast.sdpSemantics) {
          params.sdpSemantics = broadcast.sdpSemantics || 'unified-plan';
        }

        if (!broadcast.iceServers || !broadcast.iceServers.length) {
          params = null;
          broadcast.optionalArgument = null;
        }

        peer = new RTCPeerConnection(params, broadcast.optionalArgument);
      } catch (e) {
        try {
          var params = {
            iceServers: broadcast.iceServers
          };

          peer = new RTCPeerConnection(params);
        } catch (e) {
          peer = new RTCPeerConnection();
        }
      }
    } else {
      peer = config.peerRef;
    }

    if (!peer.getRemoteStreams && peer.getReceivers) {
      peer.getRemoteStreams = function() {
        var stream = new MediaStream();
        peer.getReceivers().forEach(function(receiver) {
          stream.addTrack(receiver.track);
        });
        return [stream];
      };
    }

    if (!peer.getLocalStreams && peer.getSenders) {
      peer.getLocalStreams = function() {
        var stream = new MediaStream();
        peer.getSenders().forEach(function(sender) {
          stream.addTrack(sender.track);
        });
        return [stream];
      };
    }

    peer.onicecandidate = function(event) {
      if (!event.candidate) {
        if (!broadcast.trickleIce) {
          var localSdp = peer.localDescription;
          config.onLocalSdp({
            type: localSdp.type,
            sdp: localSdp.sdp,
            remotePeerSdpConstraints: config.remotePeerSdpConstraints || false,
            renegotiatingPeer: !!config.renegotiatingPeer || false,
            broadcastDescription: self.broadcastDescription,
            dontGetRemoteStream: !!config.dontGetRemoteStream,
            extra: broadcast ? broadcast.extra : {},
            streamsToShare: streamsToShare
          });
        }
        return;
      }

      if (!broadcast.trickleIce) return;
      config.onLocalCandidate({
        candidate: event.candidate.candidate,
        sdpMid: event.candidate.sdpMid,
        sdpMLineIndex: event.candidate.sdpMLineIndex
      });
    };

    localStreams.forEach(function(localStream) {
      if (config.remoteSdp && config.remoteSdp.remotePeerSdpConstraints && config.remoteSdp.remotePeerSdpConstraints.dontGetRemoteStream) {
        return;
      }

      if (config.dontAttachLocalStream) {
        return;
      }

      localStream = broadcast.beforeAddingStream(localStream, self);

      if (!localStream) return;

      peer.getLocalStreams().forEach(function(stream) {
        if (localStream && stream.id == localStream.id) {
          localStream = null;
        }
      });

      if (localStream && localStream.getTracks) {
        localStream.getTracks().forEach(function(track) {
          try {
            // last parameter is redundant for unified-plan
            // starting from chrome version 72
            peer.addTrack(track, localStream);
          } catch (e) {}
        });
      }
    });

    peer.onicebroadcaststatechange = peer.onsignalingstatechange = function() {
      var extra = self.extra;
      if (broadcast.peers[self.userid]) {
        extra = broadcast.peers[self.userid].extra || extra;
      }

      if (!peer) {
        return;
      }

      config.onPeerStateChanged({
        iceConnectionState: peer.iceConnectionState,
        iceGatheringState: peer.iceGatheringState,
        signalingState: peer.signalingState,
        extra: extra,
        userid: self.userid
      });

      if (peer && peer.iceConnectionState && peer.iceConnectionState.search(/closed|failed/gi) !== -1 && self.streams instanceof Array) {
        self.streams.forEach(function(stream) {
          var streamEvent = broadcast.streamEvents[stream.id] || {
            streamid: stream.id,
            stream: stream,
            type: 'remote'
          };

          broadcast.onstreamended(streamEvent);
        });
      }
    };

    var sdpConstraints = {
      OfferToReceiveAudio: !!localStreams.length,
      OfferToReceiveVideo: !!localStreams.length
    };

    if (config.localPeerSdpConstraints) sdpConstraints = config.localPeerSdpConstraints;

    defaults.sdpConstraints = setSdpConstraints(sdpConstraints);

    var streamObject;
    var dontDuplicate = {};

    peer.ontrack = function(event) {
      if (!event || event.type !== 'track') return;

      event.stream = event.streams[event.streams.length - 1];

      if (!event.stream.id) {
        event.stream.id = event.track.id;
      }

      if (dontDuplicate[event.stream.id] && DetectRTC.browser.name !== 'Safari') {
        if (event.track) {
          event.track.onended = function() { // event.track.onmute = 
            peer && peer.onremovestream(event);
          };
        }
        return;
      }

      dontDuplicate[event.stream.id] = event.stream.id;

      var streamsToShare = {};
      if (config.remoteSdp && config.remoteSdp.streamsToShare) {
        streamsToShare = config.remoteSdp.streamsToShare;
      } else if (config.streamsToShare) {
        streamsToShare = config.streamsToShare;
      }

      var streamToShare = streamsToShare[event.stream.id];
      if (streamToShare) {
        event.stream.isAudio = streamToShare.isAudio;
        event.stream.isVideo = streamToShare.isVideo;
        event.stream.isScreen = streamToShare.isScreen;
      } else {
        event.stream.isVideo = !!getTracks(event.stream, 'video').length;
        event.stream.isAudio = !event.stream.isVideo;
        event.stream.isScreen = false;
      }

      event.stream.streamid = event.stream.id;

      allRemoteStreams[event.stream.id] = event.stream;
      config.onRemoteStream(event.stream);

      event.stream.getTracks().forEach(function(track) {
        track.onended = function() { // track.onmute = 
          peer && peer.onremovestream(event);
        };
      });

      event.stream.onremovetrack = function() {
        peer && peer.onremovestream(event);
      };
    };

    peer.onremovestream = function(event) {
      // this event doesn't works anymore
      event.stream.streamid = event.stream.id;

      if (allRemoteStreams[event.stream.id]) {
        delete allRemoteStreams[event.stream.id];
      }

      config.onRemoteStreamRemoved(event.stream);
    };

    if (typeof peer.removeStream !== 'function') {
      // removeStream backward compatibility
      peer.removeStream = function(stream) {
        stream.getTracks().forEach(function(track) {
          peer.removeTrack(track, stream);
        });
      };
    }

    this.addRemoteCandidate = function(remoteCandidate) {
      peer.addIceCandidate(new RTCIceCandidate(remoteCandidate));
    };

    function oldAddRemoteSdp(remoteSdp, cb) {
      cb = cb || function() {};

      if (DetectRTC.browser.name !== 'Safari') {
        remoteSdp.sdp = broadcast.processSdp(remoteSdp.sdp);
      }
      peer.setRemoteDescription(new RTCSessionDescription(remoteSdp), cb, function(error) {
        if (broadcast.enableLogs) {
          console.error('setRemoteDescription failed', '\n', error, '\n', remoteSdp.sdp);
        }

        cb();
      });
    }

    this.addRemoteSdp = function(remoteSdp, cb) {
      cb = cb || function() {};

      if (DetectRTC.browser.name !== 'Safari') {
        remoteSdp.sdp = broadcast.processSdp(remoteSdp.sdp);
      }

      peer.setRemoteDescription(new RTCSessionDescription(remoteSdp)).then(cb, function(error) {
        if (broadcast.enableLogs) {
          console.error('setRemoteDescription failed', '\n', error, '\n', remoteSdp.sdp);
        }

        cb();
      }).catch(function(error) {
        if (broadcast.enableLogs) {
          console.error('setRemoteDescription failed', '\n', error, '\n', remoteSdp.sdp);
        }

        cb();
      });
    };

    var isOfferer = true;

    if (config.remoteSdp) {
      isOfferer = false;
    }

    this.createDataChannel = function() {
      var channel = peer.createDataChannel('sctp', {});
      setChannelEvents(channel);
    };

    if (broadcast.session.data === true && !renegotiatingPeer) {
      if (!isOfferer) {
        peer.ondatachannel = function(event) {
          var channel = event.channel;
          setChannelEvents(channel);
        };
      } else {
        this.createDataChannel();
      }
    }

    this.enableDisableVideoEncoding = function(enable) {
      var rtcp;
      peer.getSenders().forEach(function(sender) {
        if (!rtcp && sender.track.kind === 'video') {
          rtcp = sender;
        }
      });

      if (!rtcp || !rtcp.getParameters) return;

      var parameters = rtcp.getParameters();
      parameters.encodings[1] && (parameters.encodings[1].active = !!enable);
      parameters.encodings[2] && (parameters.encodings[2].active = !!enable);
      rtcp.setParameters(parameters);
    };

    if (config.remoteSdp) {
      if (config.remoteSdp.remotePeerSdpConstraints) {
        sdpConstraints = config.remoteSdp.remotePeerSdpConstraints;
      }
      defaults.sdpConstraints = setSdpConstraints(sdpConstraints);
      this.addRemoteSdp(config.remoteSdp, function() {
        createOfferOrAnswer('createAnswer');
      });
    }

    function setChannelEvents(channel) {
      // force ArrayBuffer in Firefox; which uses "Blob" by default.
      channel.binaryType = 'arraybuffer';

      channel.onmessage = function(event) {
        config.onDataChannelMessage(event.data);
      };

      channel.onopen = function() {
        config.onDataChannelOpened(channel);
      };

      channel.onerror = function(error) {
        config.onDataChannelError(error);
      };

      channel.onclose = function(event) {
        config.onDataChannelClosed(event);
      };

      channel.internalSend = channel.send;
      channel.send = function(data) {
        if (channel.readyState !== 'open') {
          return;
        }

        channel.internalSend(data);
      };

      peer.channel = channel;
    }

    if (broadcast.session.audio == 'two-way' || broadcast.session.video == 'two-way' || broadcast.session.screen == 'two-way') {
      defaults.sdpConstraints = setSdpConstraints({
        OfferToReceiveAudio: broadcast.session.audio == 'two-way' || (config.remoteSdp && config.remoteSdp.remotePeerSdpConstraints && config.remoteSdp.remotePeerSdpConstraints.OfferToReceiveAudio),
        OfferToReceiveVideo: broadcast.session.video == 'two-way' || broadcast.session.screen == 'two-way' || (config.remoteSdp && config.remoteSdp.remotePeerSdpConstraints && config.remoteSdp.remotePeerSdpConstraints.OfferToReceiveAudio)
      });
    }

    var streamsToShare = {};
    peer.getLocalStreams().forEach(function(stream) {
      streamsToShare[stream.streamid] = {
        isAudio: !!stream.isAudio,
        isVideo: !!stream.isVideo,
        isScreen: !!stream.isScreen
      };
    });

    function oldCreateOfferOrAnswer(_method) {
      peer[_method](function(localSdp) {
        if (DetectRTC.browser.name !== 'Safari') {
          localSdp.sdp = broadcast.processSdp(localSdp.sdp);
        }
        peer.setLocalDescription(localSdp, function() {
          if (!broadcast.trickleIce) return;

          config.onLocalSdp({
            type: localSdp.type,
            sdp: localSdp.sdp,
            remotePeerSdpConstraints: config.remotePeerSdpConstraints || false,
            renegotiatingPeer: !!config.renegotiatingPeer || false,
            broadcastDescription: self.broadcastDescription,
            dontGetRemoteStream: !!config.dontGetRemoteStream,
            extra: broadcast ? broadcast.extra : {},
            streamsToShare: streamsToShare
          });

          broadcast.onSettingLocalDescription(self);
        }, function(error) {
          if (broadcast.enableLogs) {
            console.error('setLocalDescription-error', error);
          }
        });
      }, function(error) {
        if (broadcast.enableLogs) {
          console.error('sdp-' + _method + '-error', error);
        }
      }, defaults.sdpConstraints);
    }

    function createOfferOrAnswer(_method) {
      peer[_method](defaults.sdpConstraints).then(function(localSdp) {
        if (DetectRTC.browser.name !== 'Safari') {
          localSdp.sdp = broadcast.processSdp(localSdp.sdp);
        }
        peer.setLocalDescription(localSdp).then(function() {
          if (!broadcast.trickleIce) return;

          config.onLocalSdp({
            type: localSdp.type,
            sdp: localSdp.sdp,
            remotePeerSdpConstraints: config.remotePeerSdpConstraints || false,
            renegotiatingPeer: !!config.renegotiatingPeer || false,
            broadcastDescription: self.broadcastDescription,
            dontGetRemoteStream: !!config.dontGetRemoteStream,
            extra: broadcast ? broadcast.extra : {},
            streamsToShare: streamsToShare
          });

          broadcast.onSettingLocalDescription(self);
        }, function(error) {
          if (!broadcast.enableLogs) return;
          console.error('setLocalDescription error', error);
        });
      }, function(error) {
        if (broadcast.enableLogs) {
          console.error('sdp-error', error);
        }
      });
    }

    if (isOfferer) {
      createOfferOrAnswer('createOffer');
    }

    peer.nativeClose = peer.close;
    peer.close = function() {
      if (!peer) {
        return;
      }

      try {
        if (peer.nativeClose !== peer.close) {
          peer.nativeClose();
        }
      } catch (e) {}

      peer = null;
      self.peer = null;
    };

    this.peer = peer;
  }

  // CodecsHandler.js

  var CodecsHandler = (function() {
    function preferCodec(sdp, codecName) {
      var info = splitLines(sdp);

      if (!info.videoCodecNumbers) {
        return sdp;
      }

      if (codecName === 'vp8' && info.vp8LineNumber === info.videoCodecNumbers[0]) {
        return sdp;
      }

      if (codecName === 'vp9' && info.vp9LineNumber === info.videoCodecNumbers[0]) {
        return sdp;
      }

      if (codecName === 'h264' && info.h264LineNumber === info.videoCodecNumbers[0]) {
        return sdp;
      }

      sdp = preferCodecHelper(sdp, codecName, info);

      return sdp;
    }

    function preferCodecHelper(sdp, codec, info, ignore) {
      var preferCodecNumber = '';

      if (codec === 'vp8') {
        if (!info.vp8LineNumber) {
          return sdp;
        }
        preferCodecNumber = info.vp8LineNumber;
      }

      if (codec === 'vp9') {
        if (!info.vp9LineNumber) {
          return sdp;
        }
        preferCodecNumber = info.vp9LineNumber;
      }

      if (codec === 'h264') {
        if (!info.h264LineNumber) {
          return sdp;
        }

        preferCodecNumber = info.h264LineNumber;
      }

      var newLine = info.videoCodecNumbersOriginal.split('SAVPF')[0] + 'SAVPF ';

      var newOrder = [preferCodecNumber];

      if (ignore) {
        newOrder = [];
      }

      info.videoCodecNumbers.forEach(function(codecNumber) {
        if (codecNumber === preferCodecNumber) return;
        newOrder.push(codecNumber);
      });

      newLine += newOrder.join(' ');

      sdp = sdp.replace(info.videoCodecNumbersOriginal, newLine);
      return sdp;
    }

    function splitLines(sdp) {
      var info = {};
      sdp.split('\n').forEach(function(line) {
        if (line.indexOf('m=video') === 0) {
          info.videoCodecNumbers = [];
          line.split('SAVPF')[1].split(' ').forEach(function(codecNumber) {
            codecNumber = codecNumber.trim();
            if (!codecNumber || !codecNumber.length) return;
            info.videoCodecNumbers.push(codecNumber);
            info.videoCodecNumbersOriginal = line;
          });
        }

        if (line.indexOf('VP8/90000') !== -1 && !info.vp8LineNumber) {
          info.vp8LineNumber = line.replace('a=rtpmap:', '').split(' ')[0];
        }

        if (line.indexOf('VP9/90000') !== -1 && !info.vp9LineNumber) {
          info.vp9LineNumber = line.replace('a=rtpmap:', '').split(' ')[0];
        }

        if (line.indexOf('H264/90000') !== -1 && !info.h264LineNumber) {
          info.h264LineNumber = line.replace('a=rtpmap:', '').split(' ')[0];
        }
      });

      return info;
    }

    function removeVPX(sdp) {
      var info = splitLines(sdp);

      // last parameter below means: ignore these codecs
      sdp = preferCodecHelper(sdp, 'vp9', info, true);
      sdp = preferCodecHelper(sdp, 'vp8', info, true);

      return sdp;
    }

    function disableNACK(sdp) {
      if (!sdp || typeof sdp !== 'string') {
        throw 'Invalid arguments.';
      }

      sdp = sdp.replace('a=rtcp-fb:126 nack\r\n', '');
      sdp = sdp.replace('a=rtcp-fb:126 nack pli\r\n', 'a=rtcp-fb:126 pli\r\n');
      sdp = sdp.replace('a=rtcp-fb:97 nack\r\n', '');
      sdp = sdp.replace('a=rtcp-fb:97 nack pli\r\n', 'a=rtcp-fb:97 pli\r\n');

      return sdp;
    }

    function prioritize(codecMimeType, peer) {
      if (!peer || !peer.getSenders || !peer.getSenders().length) {
        return;
      }

      if (!codecMimeType || typeof codecMimeType !== 'string') {
        throw 'Invalid arguments.';
      }

      peer.getSenders().forEach(function(sender) {
        var params = sender.getParameters();
        for (var i = 0; i < params.codecs.length; i++) {
          if (params.codecs[i].mimeType == codecMimeType) {
            params.codecs.unshift(params.codecs.splice(i, 1));
            break;
          }
        }
        sender.setParameters(params);
      });
    }

    function removeNonG722(sdp) {
      return sdp.replace(/m=audio ([0-9]+) RTP\/SAVPF ([0-9 ]*)/g, 'm=audio $1 RTP\/SAVPF 9');
    }

    function setBAS(sdp, bandwidth, isScreen) {
      if (!bandwidth) {
        return sdp;
      }

      if (typeof isFirefox !== 'undefined' && isFirefox) {
        return sdp;
      }

      if (isScreen) {
        if (!bandwidth.screen) {
          console.warn('It seems that you are not using bandwidth for screen. Screen sharing is expected to fail.');
        } else if (bandwidth.screen < 300) {
          console.warn('It seems that you are using wrong bandwidth value for screen. Screen sharing is expected to fail.');
        }
      }

      // if screen; must use at least 300kbs
      if (bandwidth.screen && isScreen) {
        sdp = sdp.replace(/b=AS([^\r\n]+\r\n)/g, '');
        sdp = sdp.replace(/a=mid:video\r\n/g, 'a=mid:video\r\nb=AS:' + bandwidth.screen + '\r\n');
      }

      // remove existing bandwidth lines
      if (bandwidth.audio || bandwidth.video) {
        sdp = sdp.replace(/b=AS([^\r\n]+\r\n)/g, '');
      }

      if (bandwidth.audio) {
        sdp = sdp.replace(/a=mid:audio\r\n/g, 'a=mid:audio\r\nb=AS:' + bandwidth.audio + '\r\n');
      }

      if (bandwidth.screen) {
        sdp = sdp.replace(/a=mid:video\r\n/g, 'a=mid:video\r\nb=AS:' + bandwidth.screen + '\r\n');
      } else if (bandwidth.video) {
        sdp = sdp.replace(/a=mid:video\r\n/g, 'a=mid:video\r\nb=AS:' + bandwidth.video + '\r\n');
      }

      return sdp;
    }

    // Find the line in sdpLines that starts with |prefix|, and, if specified,
    // contains |substr| (case-insensitive search).
    function findLine(sdpLines, prefix, substr) {
      return findLineInRange(sdpLines, 0, -1, prefix, substr);
    }

    // Find the line in sdpLines[startLine...endLine - 1] that starts with |prefix|
    // and, if specified, contains |substr| (case-insensitive search).
    function findLineInRange(sdpLines, startLine, endLine, prefix, substr) {
      var realEndLine = endLine !== -1 ? endLine : sdpLines.length;
      for (var i = startLine; i < realEndLine; ++i) {
        if (sdpLines[i].indexOf(prefix) === 0) {
          if (!substr ||
                        sdpLines[i].toLowerCase().indexOf(substr.toLowerCase()) !== -1) {
            return i;
          }
        }
      }
      return null;
    }

    // Gets the codec payload type from an a=rtpmap:X line.
    function getCodecPayloadType(sdpLine) {
      var pattern = new RegExp('a=rtpmap:(\\d+) \\w+\\/\\d+');
      var result = sdpLine.match(pattern);
      return (result && result.length === 2) ? result[1] : null;
    }

    function setVideoBitrates(sdp, params) {
      params = params || {};
      var xgoogle_min_bitrate = params.min;
      var xgoogle_max_bitrate = params.max;

      var sdpLines = sdp.split('\r\n');

      // VP8
      var vp8Index = findLine(sdpLines, 'a=rtpmap', 'VP8/90000');
      var vp8Payload;
      if (vp8Index) {
        vp8Payload = getCodecPayloadType(sdpLines[vp8Index]);
      }

      if (!vp8Payload) {
        return sdp;
      }

      var rtxIndex = findLine(sdpLines, 'a=rtpmap', 'rtx/90000');
      var rtxPayload;
      if (rtxIndex) {
        rtxPayload = getCodecPayloadType(sdpLines[rtxIndex]);
      }

      if (!rtxIndex) {
        return sdp;
      }

      var rtxFmtpLineIndex = findLine(sdpLines, 'a=fmtp:' + rtxPayload.toString());
      if (rtxFmtpLineIndex !== null) {
        var appendrtxNext = '\r\n';
        appendrtxNext += 'a=fmtp:' + vp8Payload + ' x-google-min-bitrate=' + (xgoogle_min_bitrate || '228') + '; x-google-max-bitrate=' + (xgoogle_max_bitrate || '228');
        sdpLines[rtxFmtpLineIndex] = sdpLines[rtxFmtpLineIndex].concat(appendrtxNext);
        sdp = sdpLines.join('\r\n');
      }

      return sdp;
    }

    function setOpusAttributes(sdp, params) {
      params = params || {};

      var sdpLines = sdp.split('\r\n');

      // Opus
      var opusIndex = findLine(sdpLines, 'a=rtpmap', 'opus/48000');
      var opusPayload;
      if (opusIndex) {
        opusPayload = getCodecPayloadType(sdpLines[opusIndex]);
      }

      if (!opusPayload) {
        return sdp;
      }

      var opusFmtpLineIndex = findLine(sdpLines, 'a=fmtp:' + opusPayload.toString());
      if (opusFmtpLineIndex === null) {
        return sdp;
      }

      var appendOpusNext = '';
      appendOpusNext += '; stereo=' + (typeof params.stereo != 'undefined' ? params.stereo : '1');
      appendOpusNext += '; sprop-stereo=' + (typeof params['sprop-stereo'] != 'undefined' ? params['sprop-stereo'] : '1');

      if (typeof params.maxaveragebitrate != 'undefined') {
        appendOpusNext += '; maxaveragebitrate=' + (params.maxaveragebitrate || 128 * 1024 * 8);
      }

      if (typeof params.maxplaybackrate != 'undefined') {
        appendOpusNext += '; maxplaybackrate=' + (params.maxplaybackrate || 128 * 1024 * 8);
      }

      if (typeof params.cbr != 'undefined') {
        appendOpusNext += '; cbr=' + (typeof params.cbr != 'undefined' ? params.cbr : '1');
      }

      if (typeof params.useinbandfec != 'undefined') {
        appendOpusNext += '; useinbandfec=' + params.useinbandfec;
      }

      if (typeof params.usedtx != 'undefined') {
        appendOpusNext += '; usedtx=' + params.usedtx;
      }

      if (typeof params.maxptime != 'undefined') {
        appendOpusNext += '\r\na=maxptime:' + params.maxptime;
      }

      sdpLines[opusFmtpLineIndex] = sdpLines[opusFmtpLineIndex].concat(appendOpusNext);

      sdp = sdpLines.join('\r\n');
      return sdp;
    }

    // forceStereoAudio => via webrtcexample.com
    // requires getUserMedia => echoCancellation:false
    function forceStereoAudio(sdp) {
      var sdpLines = sdp.split('\r\n');
      var fmtpLineIndex = null;
      for (var i = 0; i < sdpLines.length; i++) {
        if (sdpLines[i].search('opus/48000') !== -1) {
          var opusPayload = extractSdp(sdpLines[i], /:(\d+) opus\/48000/i);
          break;
        }
      }
      for (var i = 0; i < sdpLines.length; i++) {
        if (sdpLines[i].search('a=fmtp') !== -1) {
          var payload = extractSdp(sdpLines[i], /a=fmtp:(\d+)/);
          if (payload === opusPayload) {
            fmtpLineIndex = i;
            break;
          }
        }
      }
      if (fmtpLineIndex === null) return sdp;
      sdpLines[fmtpLineIndex] = sdpLines[fmtpLineIndex].concat('; stereo=1; sprop-stereo=1');
      sdp = sdpLines.join('\r\n');
      return sdp;
    }

    return {
      removeVPX: removeVPX,
      disableNACK: disableNACK,
      prioritize: prioritize,
      removeNonG722: removeNonG722,
      setApplicationSpecificBandwidth: function(sdp, bandwidth, isScreen) {
        return setBAS(sdp, bandwidth, isScreen);
      },
      setVideoBitrates: function(sdp, params) {
        return setVideoBitrates(sdp, params);
      },
      setOpusAttributes: function(sdp, params) {
        return setOpusAttributes(sdp, params);
      },
      preferVP9: function(sdp) {
        return preferCodec(sdp, 'vp9');
      },
      preferCodec: preferCodec,
      forceStereoAudio: forceStereoAudio
    };
  })();

  // backward compatibility
  window.BandwidthHandler = CodecsHandler;

  // OnIceCandidateHandler.js

  var OnIceCandidateHandler = (function() {
    function processCandidates(broadcast, icePair) {
      var candidate = icePair.candidate;

      var iceRestrictions = broadcast.candidates;
      var stun = iceRestrictions.stun;
      var turn = iceRestrictions.turn;

      if (!isNull(iceRestrictions.reflexive)) {
        stun = iceRestrictions.reflexive;
      }

      if (!isNull(iceRestrictions.relay)) {
        turn = iceRestrictions.relay;
      }

      if (!iceRestrictions.host && !!candidate.match(/typ host/g)) {
        return;
      }

      if (!turn && !!candidate.match(/typ relay/g)) {
        return;
      }

      if (!stun && !!candidate.match(/typ srflx/g)) {
        return;
      }

      var protocol = broadcast.iceProtocols;

      if (!protocol.udp && !!candidate.match(/ udp /g)) {
        return;
      }

      if (!protocol.tcp && !!candidate.match(/ tcp /g)) {
        return;
      }

      if (broadcast.enableLogs) {
        console.debug('Your candidate pairs:', candidate);
      }

      return {
        candidate: candidate,
        sdpMid: icePair.sdpMid,
        sdpMLineIndex: icePair.sdpMLineIndex
      };
    }

    return {
      processCandidates: processCandidates
    };
  })();

  // IceServersHandler.js

  var IceServersHandler = (function() {
    function getIceServers(broadcast) {
      // resiprocate: 3344+4433
      // pions: 7575
      var iceServers = [{
        'urls': [
          'stun:stun.l.google.com:19302',
          'stun:stun1.l.google.com:19302',
          'stun:stun2.l.google.com:19302',
          'stun:stun.l.google.com:19302?transport=udp',
        ]
      }];

      return iceServers;
    }

    return {
      getIceServers: getIceServers
    };
  })();

  // getUserMediaHandler.js

  function setStreamType(constraints, stream) {
    if (constraints.mandatory && constraints.mandatory.chromeMediaSource) {
      stream.isScreen = true;
    } else if (constraints.mozMediaSource || constraints.mediaSource) {
      stream.isScreen = true;
    } else if (constraints.video) {
      stream.isVideo = true;
    } else if (constraints.audio) {
      stream.isAudio = true;
    }
  }

  // allow users to manage this object (to support re-capturing of screen/etc.)
  window.currentUserMediaRequest = {
    streams: [],
    mutex: false,
    queueRequests: [],
    remove: function(idInstance) {
      this.mutex = false;

      var stream = this.streams[idInstance];
      if (!stream) {
        return;
      }

      stream = stream.stream;

      var options = stream.currentUserMediaRequestOptions;

      if (this.queueRequests.indexOf(options)) {
        delete this.queueRequests[this.queueRequests.indexOf(options)];
        this.queueRequests = removeNullEntries(this.queueRequests);
      }

      this.streams[idInstance].stream = null;
      delete this.streams[idInstance];
    }
  };

  function getUserMediaHandler(options) {
    if (currentUserMediaRequest.mutex === true) {
      currentUserMediaRequest.queueRequests.push(options);
      return;
    }
    currentUserMediaRequest.mutex = true;

    // easy way to match
    var idInstance = JSON.stringify(options.localMediaConstraints);

    function streaming(stream, returnBack) {
      setStreamType(options.localMediaConstraints, stream);

      var streamEndedEvent = 'ended';

      if ('oninactive' in stream) {
        streamEndedEvent = 'inactive';
      }
      stream.addEventListener(streamEndedEvent, function() {
        delete currentUserMediaRequest.streams[idInstance];

        currentUserMediaRequest.mutex = false;
        if (currentUserMediaRequest.queueRequests.indexOf(options)) {
          delete currentUserMediaRequest.queueRequests[currentUserMediaRequest.queueRequests.indexOf(options)];
          currentUserMediaRequest.queueRequests = removeNullEntries(currentUserMediaRequest.queueRequests);
        }
      }, false);

      currentUserMediaRequest.streams[idInstance] = {
        stream: stream
      };
      currentUserMediaRequest.mutex = false;

      if (currentUserMediaRequest.queueRequests.length) {
        getUserMediaHandler(currentUserMediaRequest.queueRequests.shift());
      }

      // callback
      options.onGettingLocalMedia(stream, returnBack);
    }

    if (currentUserMediaRequest.streams[idInstance]) {
      streaming(currentUserMediaRequest.streams[idInstance].stream, true);
    } else {
      var isBlackBerry = !!(/BB10|BlackBerry/i.test(navigator.userAgent || ''));
      if (isBlackBerry || typeof navigator.mediaDevices === 'undefined' || typeof navigator.mediaDevices.getUserMedia !== 'function') {
        navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
        navigator.getUserMedia(options.localMediaConstraints, function(stream) {
          stream.streamid = stream.streamid || stream.id || getRandomString();
          stream.idInstance = idInstance;
          streaming(stream);
        }, function(error) {
          options.onLocalMediaError(error, options.localMediaConstraints);
        });
        return;
      }

      if (typeof navigator.mediaDevices === 'undefined') {
        navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
        var getUserMediaSuccess = function() {};
        var getUserMediaFailure = function() {};

        var getUserMediaStream, getUserMediaError;
        navigator.mediaDevices = {
          getUserMedia: function(hints) {
            navigator.getUserMedia(hints, function(getUserMediaSuccess) {
              getUserMediaSuccess(stream);
              getUserMediaStream = stream;
            }, function(error) {
              getUserMediaFailure(error);
              getUserMediaError = error;
            });

            return {
              then: function(successCB) {
                if (getUserMediaStream) {
                  successCB(getUserMediaStream);
                  return;
                }

                getUserMediaSuccess = successCB;

                return {
                  then: function(failureCB) {
                    if (getUserMediaError) {
                      failureCB(getUserMediaError);
                      return;
                    }

                    getUserMediaFailure = failureCB;
                  }
                };
              }
            };
          }
        };
      }

      if (options.localMediaConstraints.isScreen === true) {
        if (navigator.mediaDevices.getDisplayMedia) {
          navigator.mediaDevices.getDisplayMedia(options.localMediaConstraints).then(function(stream) {
            stream.streamid = stream.streamid || stream.id || getRandomString();
            stream.idInstance = idInstance;

            streaming(stream);
          }).catch(function(error) {
            options.onLocalMediaError(error, options.localMediaConstraints);
          });
        } else if (navigator.getDisplayMedia) {
          navigator.getDisplayMedia(options.localMediaConstraints).then(function(stream) {
            stream.streamid = stream.streamid || stream.id || getRandomString();
            stream.idInstance = idInstance;

            streaming(stream);
          }).catch(function(error) {
            options.onLocalMediaError(error, options.localMediaConstraints);
          });
        } else {
          throw new Error('getDisplayMedia API is not availabe in this browser.');
        }
        return;
      }

      navigator.mediaDevices.getUserMedia(options.localMediaConstraints).then(function(stream) {
        stream.streamid = stream.streamid || stream.id || getRandomString();
        stream.idInstance = idInstance;

        streaming(stream);
      }).catch(function(error) {
        options.onLocalMediaError(error, options.localMediaConstraints);
      });
    }
  }

  // StreamsHandler.js

  var StreamsHandler = (function() {
    function handleType(type) {
      if (!type) {
        return;
      }

      if (typeof type === 'string' || typeof type === 'undefined') {
        return type;
      }

      if (type.audio && type.video) {
        return null;
      }

      if (type.audio) {
        return 'audio';
      }

      if (type.video) {
        return 'video';
      }

      return;
    }

    function setHandlers(stream, syncAction, broadcast) {
      if (!stream || !stream.addEventListener) return;

      if (typeof syncAction == 'undefined' || syncAction == true) {
        var streamEndedEvent = 'ended';

        if ('oninactive' in stream) {
          streamEndedEvent = 'inactive';
        }

        stream.addEventListener(streamEndedEvent, function() {
          StreamsHandler.onSyncNeeded(this.streamid, streamEndedEvent);
        }, false);
      }

      stream.mute = function(type, isSyncAction) {
        type = handleType(type);

        if (typeof isSyncAction !== 'undefined') {
          syncAction = isSyncAction;
        }

        if (typeof type == 'undefined' || type == 'audio') {
          getTracks(stream, 'audio').forEach(function(track) {
            track.enabled = false;
            broadcast.streamEvents[stream.streamid].isAudioMuted = true;
          });
        }

        if (typeof type == 'undefined' || type == 'video') {
          getTracks(stream, 'video').forEach(function(track) {
            track.enabled = false;
          });
        }

        if (typeof syncAction == 'undefined' || syncAction == true) {
          StreamsHandler.onSyncNeeded(stream.streamid, 'mute', type);
        }

        broadcast.streamEvents[stream.streamid].muteType = type || 'both';

        fireEvent(stream, 'mute', type);
      };

      stream.unmute = function(type, isSyncAction) {
        type = handleType(type);

        if (typeof isSyncAction !== 'undefined') {
          syncAction = isSyncAction;
        }

        graduallyIncreaseVolume();

        if (typeof type == 'undefined' || type == 'audio') {
          getTracks(stream, 'audio').forEach(function(track) {
            track.enabled = true;
            broadcast.streamEvents[stream.streamid].isAudioMuted = false;
          });
        }

        if (typeof type == 'undefined' || type == 'video') {
          getTracks(stream, 'video').forEach(function(track) {
            track.enabled = true;
          });

          // make sure that video unmute doesn't affects audio
          if (typeof type !== 'undefined' && type == 'video' && broadcast.streamEvents[stream.streamid].isAudioMuted) {
            (function looper(times) {
              if (!times) {
                times = 0;
              }

              times++;

              // check until five-seconds
              if (times < 100 && broadcast.streamEvents[stream.streamid].isAudioMuted) {
                stream.mute('audio');

                setTimeout(function() {
                  looper(times);
                }, 50);
              }
            })();
          }
        }

        if (typeof syncAction == 'undefined' || syncAction == true) {
          StreamsHandler.onSyncNeeded(stream.streamid, 'unmute', type);
        }

        broadcast.streamEvents[stream.streamid].unmuteType = type || 'both';

        fireEvent(stream, 'unmute', type);
      };

      function graduallyIncreaseVolume() {
        if (!broadcast.streamEvents[stream.streamid].mediaElement) {
          return;
        }

        var mediaElement = broadcast.streamEvents[stream.streamid].mediaElement;
        mediaElement.volume = 0;
        afterEach(200, 5, function() {
          try {
            mediaElement.volume += .20;
          } catch (e) {
            mediaElement.volume = 1;
          }
        });
      }
    }

    function afterEach(setTimeoutInteval, numberOfTimes, callback, startedTimes) {
      startedTimes = (startedTimes || 0) + 1;
      if (startedTimes >= numberOfTimes) return;

      setTimeout(function() {
        callback();
        afterEach(setTimeoutInteval, numberOfTimes, callback, startedTimes);
      }, setTimeoutInteval);
    }

    return {
      setHandlers: setHandlers,
      onSyncNeeded: function(streamid, action, type) {}
    };
  })();

  // TextReceiver.js & TextSender.js

  function TextReceiver(broadcast) {
    var content = {};

    function receive(data, userid, extra) {
      // uuid is used to uniquely identify sending instance
      var uuid = data.uuid;
      if (!content[uuid]) {
        content[uuid] = [];
      }

      content[uuid].push(data.message);

      if (data.last) {
        var message = content[uuid].join('');
        if (data.isobject) {
          message = JSON.parse(message);
        }

        // latency detection
        var receivingTime = new Date().getTime();
        var latency = receivingTime - data.sendingTime;

        var e = {
          data: message,
          userid: userid,
          extra: extra,
          latency: latency
        };

        if (broadcast.autoTranslateText) {
          e.original = e.data;
          broadcast.Translator.TranslateText(e.data, function(translatedText) {
            e.data = translatedText;
            broadcast.onmessage(e);
          });
        } else {
          broadcast.onmessage(e);
        }

        delete content[uuid];
      }
    }

    return {
      receive: receive
    };
  }

  // TextSender.js
  var TextSender = {
    send: function(config) {
      var broadcast = config.broadcast;

      var channel = config.channel,
        remoteUserId = config.remoteUserId,
        initialText = config.text,
        packetSize = broadcast.chunkSize || 1000,
        textToTransfer = '',
        isobject = false;

      if (!isString(initialText)) {
        isobject = true;
        initialText = JSON.stringify(initialText);
      }

      // uuid is used to uniquely identify sending instance
      var uuid = getRandomString();
      var sendingTime = new Date().getTime();

      sendText(initialText);

      function sendText(textMessage, text) {
        var data = {
          type: 'text',
          uuid: uuid,
          sendingTime: sendingTime
        };

        if (textMessage) {
          text = textMessage;
          data.packets = parseInt(text.length / packetSize);
        }

        if (text.length > packetSize) {
          data.message = text.slice(0, packetSize);
        } else {
          data.message = text;
          data.last = true;
          data.isobject = isobject;
        }

        channel.send(data, remoteUserId);

        textToTransfer = text.slice(data.message.length);

        if (textToTransfer.length) {
          setTimeout(function() {
            sendText(null, textToTransfer);
          }, broadcast.chunkInterval || 100);
        }
      }
    }
  };

  // FileProgressBarHandler.js

  var FileProgressBarHandler = (function() {
    function handle(broadcast) {
      var progressHelper = {};

      // www.RTCMultiConnection.org/docs/onFileStart/
      broadcast.onFileStart = function(file) {
        var div = document.createElement('div');
        div.title = file.name;
        div.innerHTML = '<label>0%</label> <progress></progress>';

        if (file.remoteUserId) {
          div.innerHTML += ' (Sharing with:' + file.remoteUserId + ')';
        }

        if (!broadcast.filesContainer) {
          broadcast.filesContainer = document.body || document.documentElement;
        }

        broadcast.filesContainer.insertBefore(div, broadcast.filesContainer.firstChild);

        if (!file.remoteUserId) {
          progressHelper[file.uuid] = {
            div: div,
            progress: div.querySelector('progress'),
            label: div.querySelector('label')
          };
          progressHelper[file.uuid].progress.max = file.maxChunks;
          return;
        }

        if (!progressHelper[file.uuid]) {
          progressHelper[file.uuid] = {};
        }

        progressHelper[file.uuid][file.remoteUserId] = {
          div: div,
          progress: div.querySelector('progress'),
          label: div.querySelector('label')
        };
        progressHelper[file.uuid][file.remoteUserId].progress.max = file.maxChunks;
      };

      // www.RTCMultiConnection.org/docs/onFileProgress/
      broadcast.onFileProgress = function(chunk) {
        var helper = progressHelper[chunk.uuid];
        if (!helper) {
          return;
        }
        if (chunk.remoteUserId) {
          helper = progressHelper[chunk.uuid][chunk.remoteUserId];
          if (!helper) {
            return;
          }
        }

        helper.progress.value = chunk.currentPosition || chunk.maxChunks || helper.progress.max;
        updateLabel(helper.progress, helper.label);
      };

      // www.RTCMultiConnection.org/docs/onFileEnd/
      broadcast.onFileEnd = function(file) {
        var helper = progressHelper[file.uuid];
        if (!helper) {
          console.error('No such progress-helper element exist.', file);
          return;
        }

        if (file.remoteUserId) {
          helper = progressHelper[file.uuid][file.remoteUserId];
          if (!helper) {
            return;
          }
        }

        var div = helper.div;
        if (file.type.indexOf('image') != -1) {
          div.innerHTML = '<a href="' + file.url + '" download="' + file.name + '">Download <strong style="color:red;">' + file.name + '</strong> </a><br /><img src="' + file.url + '" title="' + file.name + '" style="max-width: 80%;">';
        } else {
          div.innerHTML = '<a href="' + file.url + '" download="' + file.name + '">Download <strong style="color:red;">' + file.name + '</strong> </a><br /><iframe src="' + file.url + '" title="' + file.name + '" style="width: 80%;border: 0;height: inherit;margin-top:1em;"></iframe>';
        }
      };

      function updateLabel(progress, label) {
        if (progress.position === -1) {
          return;
        }

        var position = +progress.position.toFixed(2).split('.')[1] || 100;
        label.innerHTML = position + '%';
      }
    }

    return {
      handle: handle
    };
  })();

  // TranslationHandler.js

  var TranslationHandler = (function() {
    function handle(broadcast) {
      broadcast.autoTranslateText = false;
      broadcast.language = 'en';
      broadcast.googKey = 'AIzaSyCgB5hmFY74WYB-EoWkhr9cAGr6TiTHrEE';

      // www.RTCMultiConnection.org/docs/Translator/
      broadcast.Translator = {
        TranslateText: function(text, callback) {
          // if(location.protocol === 'https:') return callback(text);

          var newScript = document.createElement('script');
          newScript.type = 'text/javascript';

          var sourceText = encodeURIComponent(text); // escape

          var randomNumber = 'method' + broadcast.token();
          window[randomNumber] = function(response) {
            if (response.data && response.data.translations[0] && callback) {
              callback(response.data.translations[0].translatedText);
              return;
            }

            if (response.error && response.error.message === 'Daily Limit Exceeded') {
              console.error('Text translation failed. Error message: "Daily Limit Exceeded."');
              return;
            }

            if (response.error) {
              console.error(response.error.message);
              return;
            }

            console.error(response);
          };

          var source = 'https://www.googleapis.com/language/translate/v2?key=' + broadcast.googKey + '&target=' + (broadcast.language || 'en-US') + '&callback=window.' + randomNumber + '&q=' + sourceText;
          newScript.src = source;
          document.getElementsByTagName('head')[0].appendChild(newScript);
        },
        getListOfLanguages: function(callback) {
          var xhr = new XMLHttpRequest();
          xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE) {
              var response = JSON.parse(xhr.responseText);

              if (response && response.data && response.data.languages) {
                callback(response.data.languages);
                return;
              }

              if (response.error && response.error.message === 'Daily Limit Exceeded') {
                console.error('Text translation failed. Error message: "Daily Limit Exceeded."');
                return;
              }

              if (response.error) {
                console.error(response.error.message);
                return;
              }

              console.error(response);
            }
          };
          var url = 'https://www.googleapis.com/language/translate/v2/languages?key=' + broadcast.googKey + '&target=en';
          xhr.open('GET', url, true);
          xhr.send(null);
        }
      };
    }

    return {
      handle: handle
    };
  })();

  // _____________________
  // RTCMultiConnection.js

  (function(broadcast) {
    forceOptions = forceOptions || {
      useDefaultDevices: true
    };

    broadcast.channel = broadcast.sessionid = (roomid || location.href.replace(/\/|:|#|\?|\$|\^|%|\.|`|~|!|\+|@|\[|\||]|\|*. /g, '').split('\n').join('').split('\r').join('')) + '';

    var mPeer = new MultiPeers(broadcast);

    var preventDuplicateOnStreamEvents = {};
    mPeer.onGettingLocalMedia = function(stream, callback) {
      callback = callback || function() {};

      if (preventDuplicateOnStreamEvents[stream.streamid]) {
        callback();
        return;
      }
      preventDuplicateOnStreamEvents[stream.streamid] = true;

      try {
        stream.type = 'local';
      } catch (e) {}

      broadcast.setStreamEndHandler(stream);

      getRMCMediaElement(stream, function(mediaElement) {
        mediaElement.id = stream.streamid;
        mediaElement.muted = true;
        mediaElement.volume = 0;

        if (broadcast.attachStreams.indexOf(stream) === -1) {
          broadcast.attachStreams.push(stream);
        }

        if (typeof StreamsHandler !== 'undefined') {
          StreamsHandler.setHandlers(stream, true, broadcast);
        }

        broadcast.streamEvents[stream.streamid] = {
          stream: stream,
          type: 'local',
          mediaElement: mediaElement,
          userid: broadcast.userid,
          extra: broadcast.extra,
          streamid: stream.streamid,
          isAudioMuted: true
        };

        try {
          setHarkEvents(broadcast, broadcast.streamEvents[stream.streamid]);
          setMuteHandlers(broadcast, broadcast.streamEvents[stream.streamid]);

          broadcast.onstream(broadcast.streamEvents[stream.streamid]);
        } catch (e) {
          //
        }

        callback();
      }, broadcast);
    };

    mPeer.onGettingRemoteMedia = function(stream, remoteUserId) {
      try {
        stream.type = 'remote';
      } catch (e) {}

      broadcast.setStreamEndHandler(stream, 'remote-stream');

      getRMCMediaElement(stream, function(mediaElement) {
        mediaElement.id = stream.streamid;

        if (typeof StreamsHandler !== 'undefined') {
          StreamsHandler.setHandlers(stream, false, broadcast);
        }

        broadcast.streamEvents[stream.streamid] = {
          stream: stream,
          type: 'remote',
          userid: remoteUserId,
          extra: broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra : {},
          mediaElement: mediaElement,
          streamid: stream.streamid
        };

        setMuteHandlers(broadcast, broadcast.streamEvents[stream.streamid]);

        broadcast.onstream(broadcast.streamEvents[stream.streamid]);
      }, broadcast);
    };

    mPeer.onRemovingRemoteMedia = function(stream, remoteUserId) {
      var streamEvent = broadcast.streamEvents[stream.streamid];
      if (!streamEvent) {
        streamEvent = {
          stream: stream,
          type: 'remote',
          userid: remoteUserId,
          extra: broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra : {},
          streamid: stream.streamid,
          mediaElement: broadcast.streamEvents[stream.streamid] ? broadcast.streamEvents[stream.streamid].mediaElement : null
        };
      }

      if (broadcast.peersBackup[streamEvent.userid]) {
        streamEvent.extra = broadcast.peersBackup[streamEvent.userid].extra;
      }

      broadcast.onstreamended(streamEvent);

      delete broadcast.streamEvents[stream.streamid];
    };

    mPeer.onNegotiationNeeded = function(message, remoteUserId, callback) {
      callback = callback || function() {};

      remoteUserId = remoteUserId || message.remoteUserId;
      message = message || '';

      // usually a message looks like this
      var messageToDeliver = {
        remoteUserId: remoteUserId,
        message: message,
        sender: broadcast.userid
      };

      if (message.remoteUserId && message.message && message.sender) {
        // if a code is manually passing required data
        messageToDeliver = message;
      }

      connectSocket(function() {
        broadcast.socket.emit(broadcast.socketMessageEvent, messageToDeliver, callback);
      });
    };

    function onUserLeft(remoteUserId) {
      broadcast.deletePeer(remoteUserId);
    }

    mPeer.onUserLeft = onUserLeft;
    mPeer.disconnectWith = function(remoteUserId, callback) {
      if (broadcast.socket) {
        broadcast.socket.emit('disconnect-with', remoteUserId, callback || function() {});
      }

      broadcast.deletePeer(remoteUserId);
    };

    broadcast.socketOptions = {
      // 'force new broadcast': true, // For SocketIO version < 1.0
      // 'forceNew': true, // For SocketIO version >= 1.0
      'transport': 'polling' // fixing transport:unknown issues
    };

    function connectSocket(connectCallback) {
      broadcast.socketAutoReConnect = true;

      if (broadcast.socket) { // todo: check here readySate/etc. to make sure socket is still opened
        if (connectCallback) {
          connectCallback(broadcast.socket);
        }
        return;
      }

      if (typeof SocketConnection === 'undefined') {
        if (typeof FirebaseConnection !== 'undefined') {
          window.SocketConnection = FirebaseConnection;
        } else if (typeof PubNubConnection !== 'undefined') {
          window.SocketConnection = PubNubConnection;
        } else {
          throw 'SocketConnection.js seems missed.';
        }
      }

      new SocketConnection(broadcast, function(s) {
        if (connectCallback) {
          connectCallback(broadcast.socket);
        }
      });
    }

    // 1st paramter is roomid
    // 2rd paramter is a callback function
    broadcast.openOrJoin = function(roomid, callback) {
      callback = callback || function() {};

      broadcast.checkPresence(roomid, function(isRoomExist, roomid) {
        if (isRoomExist) {
          broadcast.sessionid = roomid;

          var localPeerSdpConstraints = false;
          var remotePeerSdpConstraints = false;
          var isOneWay = !!broadcast.session.oneway;
          var isDataOnly = isData(broadcast.session);

          remotePeerSdpConstraints = {
            OfferToReceiveAudio: broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
            OfferToReceiveVideo: broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
          };

          localPeerSdpConstraints = {
            OfferToReceiveAudio: isOneWay ? !!broadcast.session.audio : broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
            OfferToReceiveVideo: isOneWay ? !!broadcast.session.video || !!broadcast.session.screen : broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
          };

          var broadcastDescription = {
            remoteUserId: broadcast.sessionid,
            message: {
              newParticipationRequest: true,
              isOneWay: isOneWay,
              isDataOnly: isDataOnly,
              localPeerSdpConstraints: localPeerSdpConstraints,
              remotePeerSdpConstraints: remotePeerSdpConstraints
            },
            sender: broadcast.userid
          };

          beforeJoin(broadcastDescription.message, function() {
            joinRoom(broadcastDescription, callback);
          });
          return;
        }

        broadcast.waitingForLocalMedia = true;
        broadcast.isInitiator = true;

        broadcast.sessionid = roomid || broadcast.sessionid;

        if (isData(broadcast.session)) {
          openRoom(callback);
          return;
        }

        broadcast.captureUserMedia(function() {
          openRoom(callback);
        });
      });
    };

    // don't allow someone to join this person until he has the media
    broadcast.waitingForLocalMedia = false;

    broadcast.open = function(roomid, callback) {
      callback = callback || function() {};

      broadcast.waitingForLocalMedia = true;
      broadcast.isInitiator = true;

      broadcast.sessionid = roomid || broadcast.sessionid;

      connectSocket(function() {
        if (isData(broadcast.session)) {
          openRoom(callback);
          return;
        }

        broadcast.captureUserMedia(function() {
          openRoom(callback);
        });
      });
    };

    // this object keeps extra-data records for all connected users
    // this object is never cleared so you can always access extra-data even if a user left
    broadcast.peersBackup = {};

    broadcast.deletePeer = function(remoteUserId) {
      if (!remoteUserId || !broadcast.peers[remoteUserId]) {
        return;
      }

      var eventObject = {
        userid: remoteUserId,
        extra: broadcast.peers[remoteUserId] ? broadcast.peers[remoteUserId].extra : {}
      };

      if (broadcast.peersBackup[eventObject.userid]) {
        eventObject.extra = broadcast.peersBackup[eventObject.userid].extra;
      }

      broadcast.onleave(eventObject);

      if (broadcast.peers[remoteUserId]) {
        broadcast.peers[remoteUserId].streams.forEach(function(stream) {
          stream.stop();
        });

        var peer = broadcast.peers[remoteUserId].peer;
        if (peer && peer.iceConnectionState !== 'closed') {
          try {
            peer.close();
          } catch (e) {}
        }

        if (broadcast.peers[remoteUserId]) {
          broadcast.peers[remoteUserId].peer = null;
          delete broadcast.peers[remoteUserId];
        }
      }
    };

    broadcast.rejoin = function(broadcastDescription) {
      if (broadcast.isInitiator || !broadcastDescription || !Object.keys(broadcastDescription).length) {
        return;
      }

      var extra = {};

      if (broadcast.peers[broadcastDescription.remoteUserId]) {
        extra = broadcast.peers[broadcastDescription.remoteUserId].extra;
        broadcast.deletePeer(broadcastDescription.remoteUserId);
      }

      if (broadcastDescription && broadcastDescription.remoteUserId) {
        broadcast.join(broadcastDescription.remoteUserId);

        broadcast.onReConnecting({
          userid: broadcastDescription.remoteUserId,
          extra: extra
        });
      }
    };

    broadcast.join = function(remoteUserId, options) {
      broadcast.sessionid = (remoteUserId ? remoteUserId.sessionid || remoteUserId.remoteUserId || remoteUserId : false) || broadcast.sessionid;
      broadcast.sessionid += '';

      var localPeerSdpConstraints = false;
      var remotePeerSdpConstraints = false;
      var isOneWay = false;
      var isDataOnly = false;

      if ((remoteUserId && remoteUserId.session) || !remoteUserId || typeof remoteUserId === 'string') {
        var session = remoteUserId ? remoteUserId.session || broadcast.session : broadcast.session;

        isOneWay = !!session.oneway;
        isDataOnly = isData(session);

        remotePeerSdpConstraints = {
          OfferToReceiveAudio: broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
          OfferToReceiveVideo: broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
        };

        localPeerSdpConstraints = {
          OfferToReceiveAudio: isOneWay ? !!broadcast.session.audio : broadcast.sdpConstraints.mandatory.OfferToReceiveAudio,
          OfferToReceiveVideo: isOneWay ? !!broadcast.session.video || !!broadcast.session.screen : broadcast.sdpConstraints.mandatory.OfferToReceiveVideo
        };
      }

      options = options || {};

      var cb = function() {};
      if (typeof options === 'function') {
        cb = options;
        options = {};
      }

      if (typeof options.localPeerSdpConstraints !== 'undefined') {
        localPeerSdpConstraints = options.localPeerSdpConstraints;
      }

      if (typeof options.remotePeerSdpConstraints !== 'undefined') {
        remotePeerSdpConstraints = options.remotePeerSdpConstraints;
      }

      if (typeof options.isOneWay !== 'undefined') {
        isOneWay = options.isOneWay;
      }

      if (typeof options.isDataOnly !== 'undefined') {
        isDataOnly = options.isDataOnly;
      }

      var broadcastDescription = {
        remoteUserId: broadcast.sessionid,
        message: {
          newParticipationRequest: true,
          isOneWay: isOneWay,
          isDataOnly: isDataOnly,
          localPeerSdpConstraints: localPeerSdpConstraints,
          remotePeerSdpConstraints: remotePeerSdpConstraints
        },
        sender: broadcast.userid
      };

      beforeJoin(broadcastDescription.message, function() {
        connectSocket(function() {
          joinRoom(broadcastDescription, cb);
        });
      });
      return broadcastDescription;
    };

    function joinRoom(broadcastDescription, cb) {
      broadcast.socket.emit('join-room', {
        sessionid: broadcast.sessionid,
        session: broadcast.session,
        mediaConstraints: broadcast.mediaConstraints,
        sdpConstraints: broadcast.sdpConstraints,
        streams: getStreamInfoForAdmin(),
        extra: broadcast.extra,
        password: typeof broadcast.password !== 'undefined' && typeof broadcast.password !== 'object' ? broadcast.password : ''
      }, function(isRoomJoined, error) {
        if (isRoomJoined === true) {
          if (broadcast.enableLogs) {
            console.log('isRoomJoined: ', isRoomJoined, ' roomid: ', broadcast.sessionid);
          }

          if (broadcast.peers[broadcast.sessionid]) {
            // on socket disconnect & reconnect
            return;
          }

          mPeer.onNegotiationNeeded(broadcastDescription);
        }

        if (isRoomJoined === false) {
          if (broadcast.enableLogs) {
            console.warn('isRoomJoined: ', error, ' roomid: ', broadcast.sessionid);
          }

          // [disabled] retry after 3 seconds
          false && setTimeout(function() {
            joinRoom(broadcastDescription, cb);
          }, 3000);
        }

        cb(isRoomJoined, broadcast.sessionid, error);
      });
    }

    broadcast.publicRoomIdentifier = '';

    function openRoom(callback) {
      if (broadcast.enableLogs) {
        console.log('Sending open-room signal to socket.io');
      }

      broadcast.waitingForLocalMedia = false;
      broadcast.socket.emit('open-room', {
        sessionid: broadcast.sessionid,
        session: broadcast.session,
        mediaConstraints: broadcast.mediaConstraints,
        sdpConstraints: broadcast.sdpConstraints,
        streams: getStreamInfoForAdmin(),
        extra: broadcast.extra,
        identifier: broadcast.publicRoomIdentifier,
        password: typeof broadcast.password !== 'undefined' && typeof broadcast.password !== 'object' ? broadcast.password : ''
      }, function(isRoomOpened, error) {
        if (isRoomOpened === true) {
          if (broadcast.enableLogs) {
            console.log('isRoomOpened: ', isRoomOpened, ' roomid: ', broadcast.sessionid);
          }
          callback(isRoomOpened, broadcast.sessionid);
        }

        if (isRoomOpened === false) {
          if (broadcast.enableLogs) {
            console.warn('isRoomOpened: ', error, ' roomid: ', broadcast.sessionid);
          }

          callback(isRoomOpened, broadcast.sessionid, error);
        }
      });
    }

    function getStreamInfoForAdmin() {
      try {
        return broadcast.streamEvents.selectAll('local').map(function(event) {
          return {
            streamid: event.streamid,
            tracks: event.stream.getTracks().length
          };
        });
      } catch (e) {
        return [];
      }
    }

    function beforeJoin(userPreferences, callback) {
      if (broadcast.dontCaptureUserMedia || userPreferences.isDataOnly) {
        callback();
        return;
      }

      var localMediaConstraints = {};

      if (userPreferences.localPeerSdpConstraints.OfferToReceiveAudio) {
        localMediaConstraints.audio = broadcast.mediaConstraints.audio;
      }

      if (userPreferences.localPeerSdpConstraints.OfferToReceiveVideo) {
        localMediaConstraints.video = broadcast.mediaConstraints.video;
      }

      var session = userPreferences.session || broadcast.session;

      if (session.oneway && session.audio !== 'two-way' && session.video !== 'two-way' && session.screen !== 'two-way') {
        callback();
        return;
      }

      if (session.oneway && session.audio && session.audio === 'two-way') {
        session = {
          audio: true
        };
      }

      if (session.audio || session.video || session.screen) {
        if (session.screen) {
          if (DetectRTC.browser.name === 'Edge') {
            navigator.getDisplayMedia({
              video: true,
              audio: isAudioPlusTab(broadcast)
            }).then(function(screen) {
              screen.isScreen = true;
              mPeer.onGettingLocalMedia(screen);

              if ((session.audio || session.video) && !isAudioPlusTab(broadcast)) {
                broadcast.invokeGetUserMedia(null, callback);
              } else {
                callback(screen);
              }
            }, function(error) {
              console.error('Unable to capture screen on Edge. HTTPs and version 17+ is required.');
            });
          } else {
            broadcast.invokeGetUserMedia({
              audio: isAudioPlusTab(broadcast),
              video: true,
              isScreen: true
            }, (session.audio || session.video) && !isAudioPlusTab(broadcast) ? broadcast.invokeGetUserMedia(null, callback) : callback);
          }
        } else if (session.audio || session.video) {
          broadcast.invokeGetUserMedia(null, callback, session);
        }
      }
    }

    broadcast.getUserMedia = broadcast.captureUserMedia = function(callback, sessionForced) {
      callback = callback || function() {};
      var session = sessionForced || broadcast.session;

      if (broadcast.dontCaptureUserMedia || isData(session)) {
        callback();
        return;
      }

      if (session.audio || session.video || session.screen) {
        if (session.screen) {
          if (DetectRTC.browser.name === 'Edge') {
            navigator.getDisplayMedia({
              video: true,
              audio: isAudioPlusTab(broadcast)
            }).then(function(screen) {
              screen.isScreen = true;
              mPeer.onGettingLocalMedia(screen);

              if ((session.audio || session.video) && !isAudioPlusTab(broadcast)) {
                var nonScreenSession = {};
                for (var s in session) {
                  if (s !== 'screen') {
                    nonScreenSession[s] = session[s];
                  }
                }
                broadcast.invokeGetUserMedia(sessionForced, callback, nonScreenSession);
                return;
              }
              callback(screen);
            }, function(error) {
              console.error('Unable to capture screen on Edge. HTTPs and version 17+ is required.');
            });
          } else {
            broadcast.invokeGetUserMedia({
              audio: isAudioPlusTab(broadcast),
              video: true,
              isScreen: true
            }, function(stream) {
              if ((session.audio || session.video) && !isAudioPlusTab(broadcast)) {
                var nonScreenSession = {};
                for (var s in session) {
                  if (s !== 'screen') {
                    nonScreenSession[s] = session[s];
                  }
                }
                broadcast.invokeGetUserMedia(sessionForced, callback, nonScreenSession);
                return;
              }
              callback(stream);
            });
          }
        } else if (session.audio || session.video) {
          broadcast.invokeGetUserMedia(sessionForced, callback, session);
        }
      }
    };

    broadcast.onbeforeunload = function(arg1, dontCloseSocket) {
      if (!broadcast.closeBeforeUnload) {
        return;
      }

      broadcast.peers.getAllParticipants().forEach(function(participant) {
        mPeer.onNegotiationNeeded({
          userLeft: true
        }, participant);

        if (broadcast.peers[participant] && broadcast.peers[participant].peer) {
          broadcast.peers[participant].peer.close();
        }

        delete broadcast.peers[participant];
      });

      if (!dontCloseSocket) {
        broadcast.closeSocket();
      }

      broadcast.isInitiator = false;
    };

    if (!window.ignoreBeforeUnload) {
      // user can implement its own version of window.onbeforeunload
      broadcast.closeBeforeUnload = true;
      window.addEventListener('beforeunload', broadcast.onbeforeunload, false);
    } else {
      broadcast.closeBeforeUnload = false;
    }

    broadcast.userid = getRandomString();
    broadcast.changeUserId = function(newUserId, callback) {
      callback = callback || function() {};
      broadcast.userid = newUserId || getRandomString();
      broadcast.socket.emit('changed-uuid', broadcast.userid, callback);
    };

    broadcast.extra = {};
    broadcast.attachStreams = [];

    broadcast.session = {
      audio: true,
      video: true
    };

    broadcast.enableFileSharing = false;

    // all values in kbps
    broadcast.bandwidth = {
      screen: false,
      audio: false,
      video: false
    };

    broadcast.codecs = {
      audio: 'opus',
      video: 'VP9'
    };

    broadcast.processSdp = function(sdp) {
      // ignore SDP modification if unified-pan is supported
      if (isUnifiedPlanSupportedDefault()) {
        return sdp;
      }

      if (DetectRTC.browser.name === 'Safari') {
        return sdp;
      }

      if (broadcast.codecs.video.toUpperCase() === 'VP8') {
        sdp = CodecsHandler.preferCodec(sdp, 'vp8');
      }

      if (broadcast.codecs.video.toUpperCase() === 'VP9') {
        sdp = CodecsHandler.preferCodec(sdp, 'vp9');
      }

      if (broadcast.codecs.video.toUpperCase() === 'H264') {
        sdp = CodecsHandler.preferCodec(sdp, 'h264');
      }

      if (broadcast.codecs.audio === 'G722') {
        sdp = CodecsHandler.removeNonG722(sdp);
      }

      if (DetectRTC.browser.name === 'Firefox') {
        return sdp;
      }

      if (broadcast.bandwidth.video || broadcast.bandwidth.screen) {
        sdp = CodecsHandler.setApplicationSpecificBandwidth(sdp, broadcast.bandwidth, !!broadcast.session.screen);
      }

      if (broadcast.bandwidth.video) {
        sdp = CodecsHandler.setVideoBitrates(sdp, {
          min: broadcast.bandwidth.video * 8 * 1024,
          max: broadcast.bandwidth.video * 8 * 1024
        });
      }

      if (broadcast.bandwidth.audio) {
        sdp = CodecsHandler.setOpusAttributes(sdp, {
          maxaveragebitrate: broadcast.bandwidth.audio * 8 * 1024,
          maxplaybackrate: broadcast.bandwidth.audio * 8 * 1024,
          stereo: 1,
          maxptime: 3
        });
      }

      return sdp;
    };

    if (typeof CodecsHandler !== 'undefined') {
      broadcast.BandwidthHandler = broadcast.CodecsHandler = CodecsHandler;
    }

    broadcast.mediaConstraints = {
      audio: {
        mandatory: {},
        optional: broadcast.bandwidth.audio ? [{
          bandwidth: broadcast.bandwidth.audio * 8 * 1024 || 128 * 8 * 1024
        }] : []
      },
      video: {
        mandatory: {},
        optional: broadcast.bandwidth.video ? [{
          bandwidth: broadcast.bandwidth.video * 8 * 1024 || 128 * 8 * 1024
        }, {
          facingMode: 'user'
        }] : [{
          facingMode: 'user'
        }]
      }
    };

    if (DetectRTC.browser.name === 'Firefox') {
      broadcast.mediaConstraints = {
        audio: true,
        video: true
      };
    }

    if (!forceOptions.useDefaultDevices && !DetectRTC.isMobileDevice) {
      DetectRTC.load(function() {
        var lastAudioDevice, lastVideoDevice;
        // it will force RTCMultiConnection to capture last-devices
        // i.e. if external microphone is attached to system, we should prefer it over built-in devices.
        DetectRTC.MediaDevices.forEach(function(device) {
          if (device.kind === 'audioinput' && broadcast.mediaConstraints.audio !== false) {
            lastAudioDevice = device;
          }

          if (device.kind === 'videoinput' && broadcast.mediaConstraints.video !== false) {
            lastVideoDevice = device;
          }
        });

        if (lastAudioDevice) {
          if (DetectRTC.browser.name === 'Firefox') {
            if (broadcast.mediaConstraints.audio !== true) {
              broadcast.mediaConstraints.audio.deviceId = lastAudioDevice.id;
            } else {
              broadcast.mediaConstraints.audio = {
                deviceId: lastAudioDevice.id
              };
            }
            return;
          }

          if (broadcast.mediaConstraints.audio == true) {
            broadcast.mediaConstraints.audio = {
              mandatory: {},
              optional: []
            };
          }

          if (!broadcast.mediaConstraints.audio.optional) {
            broadcast.mediaConstraints.audio.optional = [];
          }

          var optional = [{
            sourceId: lastAudioDevice.id
          }];

          broadcast.mediaConstraints.audio.optional = optional.concat(broadcast.mediaConstraints.audio.optional);
        }

        if (lastVideoDevice) {
          if (DetectRTC.browser.name === 'Firefox') {
            if (broadcast.mediaConstraints.video !== true) {
              broadcast.mediaConstraints.video.deviceId = lastVideoDevice.id;
            } else {
              broadcast.mediaConstraints.video = {
                deviceId: lastVideoDevice.id
              };
            }
            return;
          }

          if (broadcast.mediaConstraints.video == true) {
            broadcast.mediaConstraints.video = {
              mandatory: {},
              optional: []
            };
          }

          if (!broadcast.mediaConstraints.video.optional) {
            broadcast.mediaConstraints.video.optional = [];
          }

          var optional = [{
            sourceId: lastVideoDevice.id
          }];

          broadcast.mediaConstraints.video.optional = optional.concat(broadcast.mediaConstraints.video.optional);
        }
      });
    }

    broadcast.sdpConstraints = {
      mandatory: {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: true
      },
      optional: [{
        VoiceActivityDetection: false
      }]
    };

    broadcast.sdpSemantics = null; // "unified-plan" or "plan-b", ref: webrtc.org/web-apis/chrome/unified-plan/
    broadcast.iceCandidatePoolSize = null; // 0
    broadcast.bundlePolicy = null; // max-bundle
    broadcast.rtcpMuxPolicy = null; // "require" or "negotiate"
    broadcast.iceTransportPolicy = null; // "relay" or "all"
    broadcast.optionalArgument = {
      optional: [{
        DtlsSrtpKeyAgreement: true
      }, {
        googImprovedWifiBwe: true
      }, {
        googScreencastMinBitrate: 300
      }, {
        googIPv6: true
      }, {
        googDscp: true
      }, {
        googCpuUnderuseThreshold: 55
      }, {
        googCpuOveruseThreshold: 85
      }, {
        googSuspendBelowMinBitrate: true
      }, {
        googCpuOveruseDetection: true
      }],
      mandatory: {}
    };

    broadcast.iceServers = IceServersHandler.getIceServers(broadcast);

    broadcast.candidates = {
      host: true,
      stun: true,
      turn: true
    };

    broadcast.iceProtocols = {
      tcp: true,
      udp: true
    };

    // EVENTs
    broadcast.onopen = function(event) {
      if (broadcast.enableLogs) {
        console.info('Data broadcast has been opened between you & ', event.userid);
      }
    };

    broadcast.onclose = function(event) {
      if (broadcast.enableLogs) {
        console.warn('Data broadcast has been closed between you & ', event.userid);
      }
    };

    broadcast.onerror = function(error) {
      if (broadcast.enableLogs) {
        console.error(error.userid, 'data-error', error);
      }
    };

    broadcast.onmessage = function(event) {
      if (broadcast.enableLogs) {
        console.debug('data-message', event.userid, event.data);
      }
    };

    broadcast.send = function(data, remoteUserId) {
      broadcast.peers.send(data, remoteUserId);
    };

    broadcast.close = broadcast.disconnect = broadcast.leave = function() {
      broadcast.onbeforeunload(false, true);
    };

    broadcast.closeEntireSession = function(callback) {
      callback = callback || function() {};
      broadcast.socket.emit('close-entire-session', function looper() {
        if (broadcast.getAllParticipants().length) {
          setTimeout(looper, 100);
          return;
        }

        broadcast.onEntireSessionClosed({
          sessionid: broadcast.sessionid,
          userid: broadcast.userid,
          extra: broadcast.extra
        });

        broadcast.changeUserId(null, function() {
          broadcast.close();
          callback();
        });
      });
    };

    broadcast.onEntireSessionClosed = function(event) {
      if (!broadcast.enableLogs) return;
      console.info('Entire session is closed: ', event.sessionid, event.extra);
    };

    broadcast.onstream = function(e) {
      var parentNode = broadcast.videosContainer;
      parentNode.insertBefore(e.mediaElement, parentNode.firstChild);
      var played = e.mediaElement.play();

      if (typeof played !== 'undefined') {
        played.catch(function() {
          /*** iOS 11 doesn't allow automatic play and rejects ***/
        }).then(function() {
          setTimeout(function() {
            e.mediaElement.play();
          }, 2000);
        });
        return;
      }

      setTimeout(function() {
        e.mediaElement.play();
      }, 2000);
    };

    broadcast.onstreamended = function(e) {
      if (!e.mediaElement) {
        e.mediaElement = document.getElementById(e.streamid);
      }

      if (!e.mediaElement || !e.mediaElement.parentNode) {
        return;
      }

      e.mediaElement.parentNode.removeChild(e.mediaElement);
    };

    broadcast.direction = 'many-to-many';

    broadcast.removeStream = function(streamid, remoteUserId) {
      var stream;
      broadcast.attachStreams.forEach(function(localStream) {
        if (localStream.id === streamid) {
          stream = localStream;
        }
      });

      if (!stream) {
        console.warn('No such stream exist.', streamid);
        return;
      }

      broadcast.peers.getAllParticipants().forEach(function(participant) {
        if (remoteUserId && participant !== remoteUserId) {
          return;
        }

        var user = broadcast.peers[participant];
        try {
          user.peer.removeStream(stream);
        } catch (e) {}
      });

      broadcast.renegotiate();
    };

    broadcast.addStream = function(session, remoteUserId) {
      if (session.getTracks) {
        if (broadcast.attachStreams.indexOf(session) === -1) {
          if (!session.streamid) {
            session.streamid = session.id;
          }

          broadcast.attachStreams.push(session);
        }
        broadcast.renegotiate(remoteUserId);
        return;
      }

      if (isData(session)) {
        broadcast.renegotiate(remoteUserId);
        return;
      }

      if (session.audio || session.video || session.screen) {
        if (session.screen) {
          if (DetectRTC.browser.name === 'Edge') {
            navigator.getDisplayMedia({
              video: true,
              audio: isAudioPlusTab(broadcast)
            }).then(function(screen) {
              screen.isScreen = true;
              mPeer.onGettingLocalMedia(screen);

              if ((session.audio || session.video) && !isAudioPlusTab(broadcast)) {
                broadcast.invokeGetUserMedia(null, function(stream) {
                  gumCallback(stream);
                });
              } else {
                gumCallback(screen);
              }
            }, function(error) {
              console.error('Unable to capture screen on Edge. HTTPs and version 17+ is required.');
            });
          } else {
            broadcast.invokeGetUserMedia({
              audio: isAudioPlusTab(broadcast),
              video: true,
              isScreen: true
            }, function(stream) {
              if ((session.audio || session.video) && !isAudioPlusTab(broadcast)) {
                broadcast.invokeGetUserMedia(null, function(stream) {
                  gumCallback(stream);
                });
              } else {
                gumCallback(stream);
              }
            });
          }
        } else if (session.audio || session.video) {
          broadcast.invokeGetUserMedia(null, gumCallback);
        }
      }

      function gumCallback(stream) {
        if (session.streamCallback) {
          session.streamCallback(stream);
        }

        broadcast.renegotiate(remoteUserId);
      }
    };

    broadcast.invokeGetUserMedia = function(localMediaConstraints, callback, session) {
      if (!session) {
        session = broadcast.session;
      }

      if (!localMediaConstraints) {
        localMediaConstraints = broadcast.mediaConstraints;
      }

      getUserMediaHandler({
        onGettingLocalMedia: function(stream) {
          var videoConstraints = localMediaConstraints.video;
          if (videoConstraints) {
            if (videoConstraints.mediaSource || videoConstraints.mozMediaSource) {
              stream.isScreen = true;
            } else if (videoConstraints.mandatory && videoConstraints.mandatory.chromeMediaSource) {
              stream.isScreen = true;
            }
          }

          if (!stream.isScreen) {
            stream.isVideo = !!getTracks(stream, 'video').length;
            stream.isAudio = !stream.isVideo && getTracks(stream, 'audio').length;
          }

          mPeer.onGettingLocalMedia(stream, function() {
            if (typeof callback === 'function') {
              callback(stream);
            }
          });
        },
        onLocalMediaError: function(error, constraints) {
          mPeer.onLocalMediaError(error, constraints);
        },
        localMediaConstraints: localMediaConstraints || {
          audio: session.audio ? localMediaConstraints.audio : false,
          video: session.video ? localMediaConstraints.video : false
        }
      });
    };

    function applyConstraints(stream, mediaConstraints) {
      if (!stream) {
        if (broadcast.enableLogs) {
          console.error('No stream to applyConstraints.');
        }
        return;
      }

      if (mediaConstraints.audio) {
        getTracks(stream, 'audio').forEach(function(track) {
          track.applyConstraints(mediaConstraints.audio);
        });
      }

      if (mediaConstraints.video) {
        getTracks(stream, 'video').forEach(function(track) {
          track.applyConstraints(mediaConstraints.video);
        });
      }
    }

    broadcast.applyConstraints = function(mediaConstraints, streamid) {
      if (!MediaStreamTrack || !MediaStreamTrack.prototype.applyConstraints) {
        alert('track.applyConstraints is NOT supported in your browser.');
        return;
      }

      if (streamid) {
        var stream;
        if (broadcast.streamEvents[streamid]) {
          stream = broadcast.streamEvents[streamid].stream;
        }
        applyConstraints(stream, mediaConstraints);
        return;
      }

      broadcast.attachStreams.forEach(function(stream) {
        applyConstraints(stream, mediaConstraints);
      });
    };

    function replaceTrack(track, remoteUserId, isVideoTrack) {
      if (remoteUserId) {
        mPeer.replaceTrack(track, remoteUserId, isVideoTrack);
        return;
      }

      broadcast.peers.getAllParticipants().forEach(function(participant) {
        mPeer.replaceTrack(track, participant, isVideoTrack);
      });
    }

    broadcast.replaceTrack = function(session, remoteUserId, isVideoTrack) {
      session = session || {};

      if (!RTCPeerConnection.prototype.getSenders) {
        broadcast.addStream(session);
        return;
      }

      if (session instanceof MediaStreamTrack) {
        replaceTrack(session, remoteUserId, isVideoTrack);
        return;
      }

      if (session instanceof MediaStream) {
        if (getTracks(session, 'video').length) {
          replaceTrack(getTracks(session, 'video')[0], remoteUserId, true);
        }

        if (getTracks(session, 'audio').length) {
          replaceTrack(getTracks(session, 'audio')[0], remoteUserId, false);
        }
        return;
      }

      if (isData(session)) {
        throw 'broadcast.replaceTrack requires audio and/or video and/or screen.';
        return;
      }

      if (session.audio || session.video || session.screen) {
        if (session.screen) {
          if (DetectRTC.browser.name === 'Edge') {
            navigator.getDisplayMedia({
              video: true,
              audio: isAudioPlusTab(broadcast)
            }).then(function(screen) {
              screen.isScreen = true;
              mPeer.onGettingLocalMedia(screen);

              if ((session.audio || session.video) && !isAudioPlusTab(broadcast)) {
                broadcast.invokeGetUserMedia(null, gumCallback);
              } else {
                gumCallback(screen);
              }
            }, function(error) {
              console.error('Unable to capture screen on Edge. HTTPs and version 17+ is required.');
            });
          } else {
            broadcast.invokeGetUserMedia({
              audio: isAudioPlusTab(broadcast),
              video: true,
              isScreen: true
            }, (session.audio || session.video) && !isAudioPlusTab(broadcast) ? broadcast.invokeGetUserMedia(null, gumCallback) : gumCallback);
          }
        } else if (session.audio || session.video) {
          broadcast.invokeGetUserMedia(null, gumCallback);
        }
      }

      function gumCallback(stream) {
        broadcast.replaceTrack(stream, remoteUserId, isVideoTrack || session.video || session.screen);
      }
    };

    broadcast.resetTrack = function(remoteUsersIds, isVideoTrack) {
      if (!remoteUsersIds) {
        remoteUsersIds = broadcast.getAllParticipants();
      }

      if (typeof remoteUsersIds == 'string') {
        remoteUsersIds = [remoteUsersIds];
      }

      remoteUsersIds.forEach(function(participant) {
        var peer = broadcast.peers[participant].peer;

        if ((typeof isVideoTrack === 'undefined' || isVideoTrack === true) && peer.lastVideoTrack) {
          broadcast.replaceTrack(peer.lastVideoTrack, participant, true);
        }

        if ((typeof isVideoTrack === 'undefined' || isVideoTrack === false) && peer.lastAudioTrack) {
          broadcast.replaceTrack(peer.lastAudioTrack, participant, false);
        }
      });
    };

    broadcast.renegotiate = function(remoteUserId) {
      if (remoteUserId) {
        mPeer.renegotiatePeer(remoteUserId);
        return;
      }

      broadcast.peers.getAllParticipants().forEach(function(participant) {
        mPeer.renegotiatePeer(participant);
      });
    };

    broadcast.setStreamEndHandler = function(stream, isRemote) {
      if (!stream || !stream.addEventListener) return;

      isRemote = !!isRemote;

      if (stream.alreadySetEndHandler) {
        return;
      }
      stream.alreadySetEndHandler = true;

      var streamEndedEvent = 'ended';

      if ('oninactive' in stream) {
        streamEndedEvent = 'inactive';
      }

      stream.addEventListener(streamEndedEvent, function() {
        if (stream.idInstance) {
          currentUserMediaRequest.remove(stream.idInstance);
        }

        if (!isRemote) {
          // reset attachStreams
          var streams = [];
          broadcast.attachStreams.forEach(function(s) {
            if (s.id != stream.id) {
              streams.push(s);
            }
          });
          broadcast.attachStreams = streams;
        }

        // broadcast.renegotiate();

        var streamEvent = broadcast.streamEvents[stream.streamid];
        if (!streamEvent) {
          streamEvent = {
            stream: stream,
            streamid: stream.streamid,
            type: isRemote ? 'remote' : 'local',
            userid: broadcast.userid,
            extra: broadcast.extra,
            mediaElement: broadcast.streamEvents[stream.streamid] ? broadcast.streamEvents[stream.streamid].mediaElement : null
          };
        }

        if (isRemote && broadcast.peers[streamEvent.userid]) {
          // reset remote "streams"
          var peer = broadcast.peers[streamEvent.userid].peer;
          var streams = [];
          peer.getRemoteStreams().forEach(function(s) {
            if (s.id != stream.id) {
              streams.push(s);
            }
          });
          broadcast.peers[streamEvent.userid].streams = streams;
        }

        if (streamEvent.userid === broadcast.userid && streamEvent.type === 'remote') {
          return;
        }

        if (broadcast.peersBackup[streamEvent.userid]) {
          streamEvent.extra = broadcast.peersBackup[streamEvent.userid].extra;
        }

        broadcast.onstreamended(streamEvent);

        delete broadcast.streamEvents[stream.streamid];
      }, false);
    };

    broadcast.onMediaError = function(error, constraints) {
      if (broadcast.enableLogs) {
        console.error(error, constraints);
      }
    };

    broadcast.autoCloseEntireSession = false;

    broadcast.filesContainer = broadcast.videosContainer = document.body || document.documentElement;
    broadcast.isInitiator = false;

    broadcast.shareFile = mPeer.shareFile;
    if (typeof FileProgressBarHandler !== 'undefined') {
      FileProgressBarHandler.handle(broadcast);
    }

    if (typeof TranslationHandler !== 'undefined') {
      TranslationHandler.handle(broadcast);
    }

    broadcast.token = getRandomString;

    broadcast.onNewParticipant = function(participantId, userPreferences) {
      broadcast.acceptParticipationRequest(participantId, userPreferences);
    };

    broadcast.acceptParticipationRequest = function(participantId, userPreferences) {
      if (userPreferences.successCallback) {
        userPreferences.successCallback();
        delete userPreferences.successCallback;
      }

      mPeer.createNewPeer(participantId, userPreferences);
    };

    if (typeof StreamsHandler !== 'undefined') {
      broadcast.StreamsHandler = StreamsHandler;
    }

    broadcast.onleave = function(userid) {};

    broadcast.invokeSelectFileDialog = function(callback) {
      var selector = new FileSelector();
      selector.accept = '*.*';
      selector.selectSingleFile(callback);
    };

    broadcast.onmute = function(e) {
      if (!e || !e.mediaElement) {
        return;
      }

      if (e.muteType === 'both' || e.muteType === 'video') {
        e.mediaElement.src = null;
        var paused = e.mediaElement.pause();
        if (typeof paused !== 'undefined') {
          paused.then(function() {
            e.mediaElement.poster = e.snapshot || 'https://cdn.webrtc-experiment.com/images/muted.png';
          });
        } else {
          e.mediaElement.poster = e.snapshot || 'https://cdn.webrtc-experiment.com/images/muted.png';
        }
      } else if (e.muteType === 'audio') {
        e.mediaElement.muted = true;
      }
    };

    broadcast.onunmute = function(e) {
      if (!e || !e.mediaElement || !e.stream) {
        return;
      }

      if (e.unmuteType === 'both' || e.unmuteType === 'video') {
        e.mediaElement.poster = null;
        e.mediaElement.srcObject = e.stream;
        e.mediaElement.play();
      } else if (e.unmuteType === 'audio') {
        e.mediaElement.muted = false;
      }
    };

    broadcast.onExtraDataUpdated = function(event) {
      event.status = 'online';
      broadcast.onUserStatusChanged(event, true);
    };

    broadcast.getAllParticipants = function(sender) {
      return broadcast.peers.getAllParticipants(sender);
    };

    if (typeof StreamsHandler !== 'undefined') {
      StreamsHandler.onSyncNeeded = function(streamid, action, type) {
        broadcast.peers.getAllParticipants().forEach(function(participant) {
          mPeer.onNegotiationNeeded({
            streamid: streamid,
            action: action,
            streamSyncNeeded: true,
            type: type || 'both'
          }, participant);
        });
      };
    }

    broadcast.connectSocket = function(callback) {
      connectSocket(callback);
    };

    broadcast.closeSocket = function() {
      try {
        io.sockets = {};
      } catch (e) {}

      if (!broadcast.socket) return;

      if (typeof broadcast.socket.disconnect === 'function') {
        broadcast.socket.disconnect();
      }

      if (typeof broadcast.socket.resetProps === 'function') {
        broadcast.socket.resetProps();
      }

      broadcast.socket = null;
    };

    broadcast.getSocket = function(callback) {
      if (!callback && broadcast.enableLogs) {
        console.warn('getSocket.callback paramter is required.');
      }

      callback = callback || function() {};

      if (!broadcast.socket) {
        connectSocket(function() {
          callback(broadcast.socket);
        });
      } else {
        callback(broadcast.socket);
      }

      return broadcast.socket; // callback is preferred over return-statement
    };

    broadcast.getRemoteStreams = mPeer.getRemoteStreams;

    var skipStreams = ['selectFirst', 'selectAll', 'forEach'];

    broadcast.streamEvents = {
      selectFirst: function(options) {
        return broadcast.streamEvents.selectAll(options)[0];
      },
      selectAll: function(options) {
        if (!options) {
          // default will always be all streams
          options = {
            local: true,
            remote: true,
            isScreen: true,
            isAudio: true,
            isVideo: true
          };
        }

        if (options == 'local') {
          options = {
            local: true
          };
        }

        if (options == 'remote') {
          options = {
            remote: true
          };
        }

        if (options == 'screen') {
          options = {
            isScreen: true
          };
        }

        if (options == 'audio') {
          options = {
            isAudio: true
          };
        }

        if (options == 'video') {
          options = {
            isVideo: true
          };
        }

        var streams = [];
        Object.keys(broadcast.streamEvents).forEach(function(key) {
          var event = broadcast.streamEvents[key];

          if (skipStreams.indexOf(key) !== -1) return;
          var ignore = true;

          if (options.local && event.type === 'local') {
            ignore = false;
          }

          if (options.remote && event.type === 'remote') {
            ignore = false;
          }

          if (options.isScreen && event.stream.isScreen) {
            ignore = false;
          }

          if (options.isVideo && event.stream.isVideo) {
            ignore = false;
          }

          if (options.isAudio && event.stream.isAudio) {
            ignore = false;
          }

          if (options.userid && event.userid === options.userid) {
            ignore = false;
          }

          if (ignore === false) {
            streams.push(event);
          }
        });

        return streams;
      }
    };

    broadcast.socketURL = '/'; // generated via config.json
    broadcast.socketMessageEvent = 'RTCMultiConnection-Message'; // generated via config.json
    broadcast.socketCustomEvent = 'RTCMultiConnection-Custom-Message'; // generated via config.json
    broadcast.DetectRTC = DetectRTC;

    broadcast.setCustomSocketEvent = function(customEvent) {
      if (customEvent) {
        broadcast.socketCustomEvent = customEvent;
      }

      if (!broadcast.socket) {
        return;
      }

      broadcast.socket.emit('set-custom-socket-event-listener', broadcast.socketCustomEvent);
    };

    broadcast.getNumberOfBroadcastViewers = function(broadcastId, callback) {
      if (!broadcast.socket || !broadcastId || !callback) return;

      broadcast.socket.emit('get-number-of-users-in-specific-broadcast', broadcastId, callback);
    };

    broadcast.onNumberOfBroadcastViewersUpdated = function(event) {
      if (!broadcast.enableLogs || !broadcast.isInitiator) return;
      console.info('Number of broadcast (', event.broadcastId, ') viewers', event.numberOfBroadcastViewers);
    };

    broadcast.onUserStatusChanged = function(event, dontWriteLogs) {
      if (!!broadcast.enableLogs && !dontWriteLogs) {
        console.info(event.userid, event.status);
      }
    };

    broadcast.getUserMediaHandler = getUserMediaHandler;
    broadcast.multiPeersHandler = mPeer;
    broadcast.enableLogs = true;
    broadcast.setCustomSocketHandler = function(customSocketHandler) {
      if (typeof SocketConnection !== 'undefined') {
        SocketConnection = customSocketHandler;
      }
    };

    // default value should be 15k because [old]Firefox's receiving limit is 16k!
    // however 64k works chrome-to-chrome
    broadcast.chunkSize = 40 * 1000;

    broadcast.maxParticipantsAllowed = 1000;

    // eject or leave single user
    broadcast.disconnectWith = mPeer.disconnectWith;

    // check if room exist on server
    // we will pass roomid to the server and wait for callback (i.e. server's response)
    broadcast.checkPresence = function(roomid, callback) {
      roomid = roomid || broadcast.sessionid;

      if (SocketConnection.name === 'SSEConnection') {
        SSEConnection.checkPresence(roomid, function(isRoomExist, _roomid, extra) {
          if (!broadcast.socket) {
            if (!isRoomExist) {
              broadcast.userid = _roomid;
            }

            broadcast.connectSocket(function() {
              callback(isRoomExist, _roomid, extra);
            });
            return;
          }
          callback(isRoomExist, _roomid);
        });
        return;
      }

      if (!broadcast.socket) {
        broadcast.connectSocket(function() {
          broadcast.checkPresence(roomid, callback);
        });
        return;
      }

      broadcast.socket.emit('check-presence', roomid + '', function(isRoomExist, _roomid, extra) {
        if (broadcast.enableLogs) {
          console.log('checkPresence.isRoomExist: ', isRoomExist, ' roomid: ', _roomid);
        }
        callback(isRoomExist, _roomid, extra);
      });
    };

    broadcast.onReadyForOffer = function(remoteUserId, userPreferences) {
      broadcast.multiPeersHandler.createNewPeer(remoteUserId, userPreferences);
    };

    broadcast.setUserPreferences = function(userPreferences) {
      if (broadcast.dontAttachStream) {
        userPreferences.dontAttachLocalStream = true;
      }

      if (broadcast.dontGetRemoteStream) {
        userPreferences.dontGetRemoteStream = true;
      }

      return userPreferences;
    };

    broadcast.updateExtraData = function() {
      broadcast.socket.emit('extra-data-updated', broadcast.extra);
    };

    broadcast.enableScalableBroadcast = false;
    broadcast.maxRelayLimitPerUser = 3; // each broadcast should serve only 3 users

    broadcast.dontCaptureUserMedia = false;
    broadcast.dontAttachStream = false;
    broadcast.dontGetRemoteStream = false;

    broadcast.onReConnecting = function(event) {
      if (broadcast.enableLogs) {
        console.info('ReConnecting with', event.userid, '...');
      }
    };

    broadcast.beforeAddingStream = function(stream) {
      return stream;
    };

    broadcast.beforeRemovingStream = function(stream) {
      return stream;
    };

    if (typeof isChromeExtensionAvailable !== 'undefined') {
      broadcast.checkIfChromeExtensionAvailable = isChromeExtensionAvailable;
    }

    if (typeof isFirefoxExtensionAvailable !== 'undefined') {
      broadcast.checkIfChromeExtensionAvailable = isFirefoxExtensionAvailable;
    }

    if (typeof getChromeExtensionStatus !== 'undefined') {
      broadcast.getChromeExtensionStatus = getChromeExtensionStatus;
    }

    broadcast.modifyScreenConstraints = function(screen_constraints) {
      return screen_constraints;
    };

    broadcast.onPeerStateChanged = function(state) {
      if (broadcast.enableLogs) {
        if (state.iceConnectionState.search(/closed|failed/gi) !== -1) {
          console.error('Peer broadcast is closed between you & ', state.userid, state.extra, 'state:', state.iceConnectionState);
        }
      }
    };

    broadcast.isOnline = true;

    listenEventHandler('online', function() {
      broadcast.isOnline = true;
    });

    listenEventHandler('offline', function() {
      broadcast.isOnline = false;
    });

    broadcast.isLowBandwidth = false;
    if (navigator && navigator.broadcast && navigator.broadcast.type) {
      broadcast.isLowBandwidth = navigator.broadcast.type.toString().toLowerCase().search(/wifi|cell/g) !== -1;
      if (broadcast.isLowBandwidth) {
        broadcast.bandwidth = {
          audio: false,
          video: false,
          screen: false
        };

        if (broadcast.mediaConstraints.audio && broadcast.mediaConstraints.audio.optional && broadcast.mediaConstraints.audio.optional.length) {
          var newArray = [];
          broadcast.mediaConstraints.audio.optional.forEach(function(opt) {
            if (typeof opt.bandwidth === 'undefined') {
              newArray.push(opt);
            }
          });
          broadcast.mediaConstraints.audio.optional = newArray;
        }

        if (broadcast.mediaConstraints.video && broadcast.mediaConstraints.video.optional && broadcast.mediaConstraints.video.optional.length) {
          var newArray = [];
          broadcast.mediaConstraints.video.optional.forEach(function(opt) {
            if (typeof opt.bandwidth === 'undefined') {
              newArray.push(opt);
            }
          });
          broadcast.mediaConstraints.video.optional = newArray;
        }
      }
    }

    broadcast.getExtraData = function(remoteUserId, callback) {
      if (!remoteUserId) throw 'remoteUserId is required.';

      if (typeof callback === 'function') {
        broadcast.socket.emit('get-remote-user-extra-data', remoteUserId, function(extra, remoteUserId, error) {
          callback(extra, remoteUserId, error);
        });
        return;
      }

      if (!broadcast.peers[remoteUserId]) {
        if (broadcast.peersBackup[remoteUserId]) {
          return broadcast.peersBackup[remoteUserId].extra;
        }
        return {};
      }

      return broadcast.peers[remoteUserId].extra;
    };

    if (forceOptions.autoOpenOrJoin) {
      broadcast.openOrJoin(broadcast.sessionid);
    }

    broadcast.onUserIdAlreadyTaken = function(useridAlreadyTaken, yourNewUserId) {
      // via #683
      broadcast.close();
      broadcast.closeSocket();

      broadcast.isInitiator = false;
      broadcast.userid = broadcast.token();

      broadcast.join(broadcast.sessionid);

      if (broadcast.enableLogs) {
        console.warn('Userid already taken.', useridAlreadyTaken, 'Your new userid:', broadcast.userid);
      }
    };

    broadcast.trickleIce = true;
    broadcast.version = '3.6.9';

    broadcast.onSettingLocalDescription = function(event) {
      if (broadcast.enableLogs) {
        console.info('Set local description for remote user', event.userid);
      }
    };

    broadcast.resetScreen = function() {
      sourceId = null;
      if (DetectRTC && DetectRTC.screen) {
        delete DetectRTC.screen.sourceId;
      }

      currentUserMediaRequest = {
        streams: [],
        mutex: false,
        queueRequests: []
      };
    };

    // if disabled, "event.mediaElement" for "onstream" will be NULL
    broadcast.autoCreateMediaElement = true;

    // set password
    broadcast.password = null;

    // set password
    broadcast.setPassword = function(password, callback) {
      callback = callback || function() {};
      if (broadcast.socket) {
        broadcast.socket.emit('set-password', password, callback);
      } else {
        broadcast.password = password;
        callback(true, broadcast.sessionid, null);
      }
    };

    broadcast.onSocketDisconnect = function(event) {
      if (broadcast.enableLogs) {
        console.warn('socket.io broadcast is closed');
      }
    };

    broadcast.onSocketError = function(event) {
      if (broadcast.enableLogs) {
        console.warn('socket.io broadcast is failed');
      }
    };

    // error messages
    broadcast.errors = {
      ROOM_NOT_AVAILABLE: 'Room not available',
      INVALID_PASSWORD: 'Invalid password',
      USERID_NOT_AVAILABLE: 'User ID does not exist',
      ROOM_PERMISSION_DENIED: 'Room permission denied',
      ROOM_FULL: 'Room full',
      DID_NOT_JOIN_ANY_ROOM: 'Did not join any room yet',
      INVALID_SOCKET: 'Invalid socket',
      PUBLIC_IDENTIFIER_MISSING: 'publicRoomIdentifier is required',
      INVALID_ADMIN_CREDENTIAL: 'Invalid username or password attempted'
    };
  })(this);

};

if (typeof module !== 'undefined' /* && !!module.exports*/ ) {
  module.exports = exports = RTCMultiConnection;
}

if (typeof define === 'function' && define.amd) {
  define('RTCMultiConnection', [], function() {
    return RTCMultiConnection;
  });
}
