from xml.etree.ElementTree import ElementTree

tree = ElementTree()
tree.parse('..\\..\\..\\conf\\webarchive.conf.xml')

def get_element(value):
    value = changeinputstring(value)
    if value == ( 'crawler/interval' or 'server/notify/interval'):
        try:
            return changeoutputstring(tree.findtext(value))
        except KeyError:
            return ''
    else:
        try:
            return tree.findtext(value)
        except KeyError:
            return ''

def changeinputstring(value):
    index = 0
    end = len(value)
    while index < end:
        if value[index] == "." :
            value = value[0:index] + '/' +  value[index+1:end]
        index=index + 1
    return value

def changeoutputstring(value):
    index = 0
    start = 0
    time = 0
    while index < len(value):
        if value[index] == ":" :
            time = time*60 + int(value[start:index])
            start = index + 1
        if index == len(value)-1:
            time = time*60 + int(value[start:index+1])
        index = index + 1
    time = str(time)
    return time

