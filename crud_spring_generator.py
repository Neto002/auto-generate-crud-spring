import os
import shutil

def create_generated_files_directory() -> None:
    print("Creating files directory if not exists...")
    if (not os.path.exists("files")):
        os.mkdir("files")


def copy_template_file(source_path: str, destination_path: str) -> None:
    print("Copying template file content to destination .java file...")
    shutil.copy(src=source_path, dst=destination_path)


def replace_template_content_in_file(package_name: str, endpoint_name: str, file_name: str, class_type: str) -> None:
    print(f"Reading template file for {class_type} class...")
    with open (f'./base_text_files/EndPoint{class_type.capitalize()}.txt', 'r', encoding='utf-8') as file:
        templated_file_content = file.read()
    
    print(f"Creating {file_name} class file...")
    with open(f'./files/{file_name}', 'w', encoding='utf-8') as file:
        formatted_file_content = templated_file_content.replace("$packageName", package_name).replace("$endpointCapitalName", endpoint_name.capitalize()).replace("$endpointName", endpoint_name)

        file.write(formatted_file_content)
    
    print(f"{file_name} class has been created succesfully!")

def create_dozer_mapper_class(package_name: str) -> None:
    print("Reading template file for DozerMapper class...")
    with open('./base_text_files/DozerMapper.txt', 'r', encoding='utf-8') as file:
        templated_file_content = file.read()
    
    print("Creating DozerMapper.java class file...")
    with open('./files/DozerMapper.java', 'w', encoding='utf-8') as file:
        formatted_file_content = templated_file_content.replace("$packageName", package_name)
        file.write(formatted_file_content)
    
    print("DozerMapper.java class has been created succesfully!")

    
def are_variables_valid(package_name, endpoint_name) -> bool:
    return True if (package_name != '' or package_name != None or endpoint_name != '' or endpoint_name != None) else False
