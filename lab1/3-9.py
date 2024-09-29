class Client:
    def __init__(self, id, name, type_of_ownership, address, phone):
        self.__id = id
        self.__name = name
        self.__type_of_ownership = type_of_ownership
        self.__address = address
        self.__phone = phone

    # Геттеры
    @property
    def id(self):
        return self.__id

    @property
    def name(self):
        return self.__name

    @property
    def type_of_ownership(self):
        return self.__type_of_ownership

    @property
    def address(self):
        return self.__address

    @property
    def phone(self):
        return self.__phone

    # Сеттеры
    @id.setter
    def id(self, id):
        self.__id = id

    @name.setter
    def name(self, name):
        self.__name = name

    @type_of_ownership.setter
    def type_of_ownership(self, type_of_ownership):
        self.__type_of_ownership = type_of_ownership

    @address.setter
    def address(self, address):
        self.__address = address

    @phone.setter
    def phone(self, phone):
        self.__phone = phone

    def __str__(self):
        return f"Client(id={self.__id}, name={self.__name}, type_of_ownership={self.__type_of_ownership}, address={self.__address}, phone={self.__phone})"
