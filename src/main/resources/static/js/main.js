
function getIndex(list, id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}

var messageApi = Vue.resource('messages{/id}');

Vue.component('message-form', {
    props: ['messages', 'messageAttr'],
    data: function () {
        return {
            text: '',
            id: ''
        }
    },
    watch: {
        messageAttr: function(newVal, oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
            '<input type="text" placeholder="Write something" v-model="text" />' +
            '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            var message = { text: this.text };

            if (this.id) {
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.messages, data.id);
                        this.messages.splice(index, 1, data);
                    })
                )
            } else {
                messageApi.save({}, message).then(result =>
                    result.json().then(data => {
                        this.messages.push(data);
                    })
                )
            }
            this.text = '';
            this.id = '';
        }
    }
});

Vue.component('message-row', {
    props: ['message', 'editMethod', 'delMethod'],
    template:
        '<div>' +
            '<i>({{ message.id }})</i> {{ message.text }}' +
            '<span style="position: absolute; right: 0">' +
                '<input type="button" value="Edit" @click="edit" />' +
                '<input type="button" value="X" @click="del" />' +
            '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.message);
        },
        del: function () {
            this.delMethod(this.message);
        }
    }
});


Vue.component('messages-list', {
    props: ['messages'],
    data: function () {
        return {
            message: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
            '<message-form :messages="messages" :messageAttr="message" />' +
            '<message-row v-for="message in messages" :key="message.id" :message="message"' +
                ' :editMethod="editMethod" :delMethod="delMethod"></message-row>' +
        '</div>',
    created: function () {
        messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(message => this.messages.push(message))
            )
        )
    },
    methods: {
        editMethod: function (message) {
            this.message = message;
        },
        delMethod: function (message) {
            messageApi.remove({id: message.id}).then(response => {
                if (response.ok) {
                    this.messages.splice(this.messages.indexOf(message), 1);
                }
            })
        }
    }
});


var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages"/>',
    data: {
        messages: []
    }
});