const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// Thumbnail prefix added to file names.
const DISPLAY_PREFIX = 'prev_';

//firebase-samples generate-thumbnail: https://github.com/firebase/functions-samples/tree/master/generate-thumbnail
/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for t`he specific language governing permissions and
 * limitations under the License.
 */
'use strict';

const mkdirp = require('mkdirp-promise');
// Include a Service Account Key to use a Signed URL
//const gcs = require('@google-cloud/storage')({keyFilename: 'service-account-credentials.json'});
const gcs = require('@google-cloud/storage')({keyFilename: 'power-rangers-811fe-firebase-adminsdk-eftqe-567eed8ddc.json'});
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
const spawn = require('child-process-promise').spawn;
const path = require('path');
const os = require('os');
const fs = require('fs');

// Max height and width of the thumbnail in pixels.
const THUMB_MAX_HEIGHT = 200;
const THUMB_MAX_WIDTH = 200;
// Thumbnail prefix added to file names.
const THUMB_PREFIX = 'thumb_';

/**
 * When an image is uploaded in the Storage bucket We generate a thumbnail automatically using
 * ImageMagick.
 * After the thumbnail has been generated and uploaded to Cloud Storage,
 * we write the public URL to the Firebase Realtime Database.
 */
exports.generateThumbnail = functions.storage.object().onChange(event => {
  // File and directory paths.
  const filePath = event.data.name;

  if (fileName.startsWith(DISPLAY_PREFIX)){

  } else {
  const fileDir = path.dirname(filePath);
  const fileName = path.basename(filePath);
  const thumbFilePath = path.normalize(path.join(fileDir, `${THUMB_PREFIX}${fileName}`));
  const tempLocalFile = path.join(os.tmpdir(), filePath);
  const tempLocalDir = path.dirname(tempLocalFile);
  const tempLocalThumbFile = path.join(os.tmpdir(), thumbFilePath);

  // Exit if this is triggered on a file that is not an image.
  if (!event.data.contentType.startsWith('image/')) {
    console.log('This is not an image.');
    return;
  }

  // Exit if the image is already a thumbnail.
  if (fileName.startsWith(THUMB_PREFIX)) {
    console.log('Already a Thumbnail.');
    return;
  }

  // Exit if this is a move or deletion event.
  if (event.data.resourceState === 'not_exists') {
    console.log('This is a deletion event.');
    return;
  }

  // Cloud Storage files.
  const bucket = gcs.bucket(event.data.bucket);
  const file = bucket.file(filePath);
  const thumbFile = bucket.file(thumbFilePath);

  // Create the temp directory where the storage file will be downloaded.
  return mkdirp(tempLocalDir).then(() => {
    // Download file from bucket.
	return file.download({destination: tempLocalFile});
	return null;
  }).then(() => {
    console.log('The file has been downloaded to', tempLocalFile);
    // Generate a thumbnail using ImageMagick.
    return spawn('convert', [tempLocalFile, '-thumbnail', `${THUMB_MAX_WIDTH}x${THUMB_MAX_HEIGHT}>`, tempLocalThumbFile]);
  }).then(() => {
    console.log('Thumbnail created at', tempLocalThumbFile);
    // Uploading the Thumbnail.
    return bucket.upload(tempLocalThumbFile, {destination: thumbFilePath});
  }).then(() => {
    console.log('Thumbnail uploaded to Storage at', thumbFilePath);
    // Once the image has been uploaded delete the local files to free up disk space.
    fs.unlinkSync(tempLocalFile);
    fs.unlinkSync(tempLocalThumbFile);
    // Get the Signed URLs for the thumbnail and original image.
    const config = {
      action: 'read',
      expires: '03-01-2500'
    };
    return Promise.all([
      thumbFile.getSignedUrl(config),
      file.getSignedUrl(config)
    ]);
  }).then(results => {
    console.log('Got Signed URLs.');
    const thumbResult = results[0];
    const originalResult = results[1];
    const thumbFileUrl = thumbResult[0];
    const fileUrl = originalResult[0];
    // Add the URLs to the Database
    return admin.database().ref('images').push({path: fileUrl, thumbnail: thumbFileUrl});
  });
  }
});

/**
 * Modified version of the generateThumbnail to scale pictures on a proper displaying size
 */
'use strict';
/**
 * When an image is uploaded in the Storage bucket We generate a thumbnail automatically using
 * ImageMagick.
 * After the thumbnail has been generated and uploaded to Cloud Storage,
 * we write the public URL to the Firebase Realtime Database.
 */
   // Max height and width of the thumbnail in pixels.
   var height;
   var width;

exports.generateDisplaySize = functions.storage.object().onChange(event => {
  // File and directory paths.
  const filePath = event.data.name;

if (fileName.startsWith(THUMB_PREFIX)){

} else {
  //if witdh > height image is landscape
  if (event.data.width > event.data.height) {
  height = 720;
  width = 1280;
  } else {
    height = 2276;
    width = 1280;
  }

  const fileDir = path.dirname(filePath);
  const fileName = path.basename(filePath);
  const displayFilePath = path.normalize(path.join(fileDir, `${DISPLAY_PREFIX}${fileName}`));
  const tempLocalFile = path.join(os.tmpdir(), filePath);
  const tempLocalDir = path.dirname(tempLocalFile);
  const tempLocalDisplayFile = path.join(os.tmpdir(), displayFilePath);

  // Exit if this is triggered on a file that is not an image.
  if (!event.data.contentType.startsWith('image/')) {
    console.log('This is not an image.');
    return;
  }

  // Exit if the image is already a thumbnail.
  if (fileName.startsWith(DISPLAY_PREFIX)) {
    console.log('Already a Displayimage.');
    return;
  }

  // Exit if this is a move or deletion event.
  if (event.data.resourceState === 'not_exists') {
    console.log('This is a deletion event.');
    return;
  }

  // Cloud Storage files.
  const bucket = gcs.bucket(event.data.bucket);
  const file = bucket.file(filePath);
  const displayFile = bucket.file(displayFilePath);

  // Create the temp directory where the storage file will be downloaded.
  return mkdirp(tempLocalDir).then(() => {
    // Download file from bucket.
	return file.download({destination: tempLocalFile});
	return null;
  }).then(() => {
    console.log('The file has been downloaded to', tempLocalFile);
    // Generate a thumbnail using ImageMagick.
    return spawn('convert', [tempLocalFile, '-thumbnail', `${width}x${height}>`, tempLocalDisplayFile]);
  }).then(() => {
    console.log('Displayimg created at', tempLocalDisplayFile);
    // Uploading the Thumbnail.
    return bucket.upload(tempLocalDisplayFile, {destination: displayFilePath});
  }).then(() => {
    console.log('Displayimg uploaded to Storage at', displayFilePath);
    // Once the image has been uploaded delete the local files to free up disk space.
    fs.unlinkSync(tempLocalFile);
    fs.unlinkSync(tempLocalDisplayFile);
    // Get the Signed URLs for the thumbnail and original image.
    const config = {
      action: 'read',
      expires: '03-01-2500'
    };
    return Promise.all([
      displayFile.getSignedUrl(config),
      file.getSignedUrl(config)
    ]);
  }).then(results => {
    console.log('Got Signed URLs.');
    const displayResult = results[0];
    const originalResult = results[1];
    const displayFileUrl = displayResult[0];
    const fileUrl = originalResult[0];
    // Add the URLs to the Database
    return admin.database().ref('images').push({path: fileUrl, display: displayFileUrl});
  });
  }
});



/*
* Listens for cases being confirmed,
* calculates number of tasks and the total reward,
* and writes both to the Firebase Database
*/
exports.createTasks = functions.database.ref('/cases/{caseId}/confirmed')
    .onWrite(event => {
          const isConfirmed = event.data.val();
          // First check if 'confirmed' value is true
          if(isConfirmed) {
            // Get the current values of areaX and areaY from the database
            const areaX = event.data.adminRef.parent.child('areaX').once('value');
            const areaY = event.data.adminRef.parent.child('areaY').once('value');
            // Get the case values
            const city = event.data.adminRef.parent.child('city').once('value');
            const country = event.data.adminRef.parent.child('country').once('value');
            const comment = event.data.adminRef.parent.child('comment').once('value');
            const title = event.data.adminRef.parent.child('name').once('value');
            const scale = event.data.adminRef.parent.child('scale').once('value');
            const caseId = event.data.adminRef.parent.child('id').once('value');
            const userId = event.data.adminRef.parent.child('userID').once('value');
            const name = event.data.adminRef.parent.child('name').once('value');

            // Get image URIs for case pictures from the database
            const casePictureList = event.data.adminRef.parent.child('pictureURL').once('value');

            /* Get database case key
            event.data.adminRef.parent.once("value").then(function(snapshot) {
              console.log("Key: " + snapshot.key);
              const caseDbId = snapshot.key;
            });
            */
            //const caseDbId = event.data.adminRef.parent.once('key');

            return Promise.all([areaX, areaY, city, country, comment, title, scale, caseId, casePictureList, userId, name]).then(results => {
              // Get list of pictures assigned to a case and remove the first one (main case picture)
              const casePictureList = results[8].val();
              const casePicture = casePictureList[0];
              casepictureList = casePictureList.shift();
              console.log("Pictures: ", casePictureList);
              // Calculate Number of rangers needed
              const number_rangers = casePictureList.length;
              console.log("Number Rangers: ", number_rangers);

              // Calculate the size of case area and reward
              var size = results[0].val() * results[1].val();
              const scale = results[6].val();
              // Total reward
              var total_reward = Math.round(size/5);
              // Reward per ranger
              var ranger_reward = Math.round(total_reward/number_rangers) + scale;
              console.log("Ranger reward: ", ranger_reward);

                // Get all other values from Promise
                const city = results[2].val();
                const country = results[3].val();
                const comment = results[4].val();
                const title = results[5].val();
                const id = results[7].val();
                const userId = results[9].val();
                const name = results[10].val();

                // Create a random task id
                var taskId = Math.random().toString(36).substring(7);

                // Create task database nodes
                casePictureList.forEach(function(uri, index) {
                  var taskRef = event.data.adminRef.root.child('tasks/').push();
                  var taskDbId = taskRef.key;
                  var taskIndex = index+1
              return taskRef.set({taskDbId: taskDbId, taskId: taskId, city: city, country: country, comment: comment, reward: ranger_reward, scale: scale, taskPicture: uri, casePicture: casePicture, caseId: id, taskCompleted: false, taskVoted: false, assigned: false, numberRangers: number_rangers, name: name + " (Area " + (taskIndex) +")"});
                });

                /*
                // Write case with its caseId into corresponding user db
                const userRef = event.data.adminRef.root.child('users/');
                userRef.orderByChild("id").equalTo(userId).then(snapshot => {
                  console.log("User DB Id: " + snapshot.ref().parent().name());
                  // Find the parent and return the key (dbId)
                  var userDBID = snapshot.data.ref.parent.key;
                  return event.data.adminRef.root.child('users/' + userDBID).child('cases/').push().set({caseId: caseId});
                });
                */

            });
          }
          else {
            console.log("Confirmed not true");
          }
});


/*
* Listens for tasks being completed, and writes upvotes and downvotes into task databse
*/
exports.completeTask = functions.database.ref('/tasks/{taskId}/taskCompleted')
    .onWrite(event => {
      const isCompleted = event.data.val();
          // First check if 'confirmed' value is true
          if(isCompleted) {
            return event.data.adminRef.parent.update({numberDownvotes: 0, numberUpvotes: 0});
          }
          else {
            console.log("Task not completed yet");
          }
});


/*
* Listens for tasks being confirmed (more than 5 upvotes), and writes isConfirmed true into db
*/
exports.upvotedTask = functions.database.ref('/tasks/{taskId}/numberUpvotes')
    .onWrite(event => {
      const numberUpvotes = event.data.val();
          // First check if 'numberUpvotes' value is greater 5
          if(numberUpvotes >= 5) {
            return event.data.adminRef.parent.update({isConfirmed: true});
          }
          else {
            console.log("Number of Upvotes: ", numberUpvotes);
          }
});

/*
* Listens for tasks being aborted (more than 5 downvotes), and writes isConfirmed false into db
*/
exports.downvotedTask = functions.database.ref('/tasks/{taskId}/numberDownvotes')
    .onWrite(event => {
      const numberDownvotes = event.data.val();
          // First check if 'numberDownvotes' value is greater 5
          if(numberDownvotes >= 5) {
            return event.data.adminRef.parent.update({isConfirmed: false});
          }
          else {
            console.log("Number of downvotes: ", numberDownvotes);
          }
});
