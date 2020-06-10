import os


def script_path(filename, folder):
    "Gets the absolute path of a file"
    filepath = os.path.join(os.path.dirname(__file__))
    return os.path.join(filepath, folder, filename)


def list_dir(folder):
    return os.listdir(script_path('.', folder))
